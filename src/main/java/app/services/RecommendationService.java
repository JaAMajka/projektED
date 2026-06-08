package app.services;

import app.Exceptions.CafeNotFoundException;
import app.Exceptions.RecommendationDoesNotBelongToUserException;
import app.Exceptions.RecommendationNotFoundException;
import app.Exceptions.UserNotFoundException;
import app.dtos.creating.CreateRecommendationDTO;
import app.dtos.responding.ResponseRecommendationDTO;
import app.dtos.updating.UpdateRecommendationDTO;
import app.mappers.RecommendationMapper;
import app.models.Cafe;
import app.models.Rate;
import app.models.Recommendation;
import app.models.User;
import app.repositories.CafeRepository;
import app.repositories.RecommendationRepository;
import app.repositories.RateRepository;
import app.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.Map;
import java.util.Set;
import java.util.Collections;
import java.util.Comparator;

@Service
@RequiredArgsConstructor
@Slf4j
public class RecommendationService {
    private final RecommendationRepository recommendationRepository;
    private final RecommendationMapper recommendationMapper;
    private final CafeRepository cafeRepository;
    private final UserRepository userRepository;
    private final CafeService cafeService;
    private final RateRepository rateRepository;

    private static final int K = 4;
    private static final int MAX_ITERATIONS = 20;

    private record CafeScores(double beverage, double service, double atmosphere) {}

    public ResponseRecommendationDTO getRecommendationDtoById(Long id, Long userId) {
        return recommendationMapper.toDto(getRecommendationById(id, userId));
    }
    private Recommendation getRecommendationById(Long id, Long userId) {
        Recommendation recommendation = recommendationRepository.findById(id).orElseThrow(() -> new RecommendationNotFoundException("Recommendation not found"));
        userRepository.findById(recommendation.getUser().getId()).orElseThrow(() -> new UserNotFoundException("User not found"));
        if(recommendation.getUser().getId().equals(userId)){
            return recommendation;
        } else {
            throw new RecommendationDoesNotBelongToUserException("Recommendation not found");
        }
    }

    public void deleteRecommendationById(Long id, Long userId) {
        Recommendation recommendation = getRecommendationById(id, userId);
        recommendationRepository.delete(recommendation);
    }
    public List<ResponseRecommendationDTO> getRecommendationsByUserId(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        return recommendationRepository.findAllByUserId(userId)
                .stream()
                .map(recommendationMapper::toDto)
                .toList();
    }
    public Recommendation createRecommendation(CreateRecommendationDTO dto) {
        Recommendation recommendation = recommendationMapper.toEntity(dto);
        Cafe cafe = cafeRepository.findById(dto.cafeId()).orElseThrow(() -> new CafeNotFoundException("Cafe not found"));
        recommendation.setCafe(cafe);
        User user = userRepository.findById(dto.userId()).orElseThrow(() -> new UserNotFoundException("User not found"));
        recommendation.setUser(user);
        return recommendationRepository.save(recommendation);
    }
    public void updateRecommendation(UpdateRecommendationDTO dto, Long recommendationId, Long userId) {
        Recommendation recommendation = getRecommendationById(recommendationId, userId);
        recommendationMapper.updateRecommendationFromDto(dto, recommendation);
        recommendationRepository.save(recommendation);
    }

    /**
     * Główna metoda generująca rekomendacje dla użytkownika na podstawie klasteryzacji K-Means
     */
    public List<Cafe> getRecommendationsForUser(Long userId, int limit) {
        List<Rate> allRates = rateRepository.findAll();
        List<Cafe> allCafes = cafeRepository.findAll();
        
        if (allRates.isEmpty() || allCafes.isEmpty()) {
            return Collections.emptyList();
        }

        // 1. Budowanie macierzy trójwymiarowej: Map<UserId, Map<CafeId, CafeScores>>
        Map<Long, Map<Long, CafeScores>> userItemMatrix = new HashMap<>();
        for (Rate rate : allRates) {
            if (rate.getAuthor() == null || rate.getCafe() == null) continue;
            
            long uId = rate.getAuthor().getId();
            long cId = rate.getCafe().getId();
            
            CafeScores scores = new CafeScores(
                rate.getBeverageScore() != null ? rate.getBeverageScore() : 3.0,
                rate.getServiceScore() != null ? rate.getServiceScore() : 3.0,
                rate.getAtmosphereScore() != null ? rate.getAtmosphereScore() : 3.0
            );
            
            userItemMatrix.putIfAbsent(uId, new HashMap<>());
            userItemMatrix.get(uId).put(cId, scores);
        }

        // Fallback: jeśli użytkownik nie ocenił jeszcze niczego, zwracamy najpopularniejsze kawiarnie
        if (!userItemMatrix.containsKey(userId)) {
            return getPopularCafesFallback(allCafes, allRates, limit);
        }

        // 2. Uruchomienie wielowymiarowego algorytmu K-Means
        List<Long> userIds = new ArrayList<>(userItemMatrix.keySet());
        Map<Integer, List<Long>> clusters = runMultiDimensionalKMeans(userIds, userItemMatrix, allCafes);

        // 3. Identyfikacja klastra docelowego użytkownika
        int targetClusterId = -1;
        for (Map.Entry<Integer, List<Long>> entry : clusters.entrySet()) {
            if (entry.getValue().contains(userId)) {
                targetClusterId = entry.getKey();
                break;
            }
        }

        if (targetClusterId == -1) {
            return getPopularCafesFallback(allCafes, allRates, limit);
        }

        // 4. Agregacja i sumowanie składowych ocen wewnątrz wybranego klastra
        List<Long> clusterMembers = clusters.get(targetClusterId);
        Map<Long, List<Double>> cafeAccumulatedScores = new HashMap<>();

        for (Long memberId : clusterMembers) {
            Map<Long, CafeScores> memberRates = userItemMatrix.get(memberId);
            for (Map.Entry<Long, CafeScores> rateEntry : memberRates.entrySet()) {
                long cafeId = rateEntry.getKey();
                CafeScores cs = rateEntry.getValue();
                
                // Sumujemy wektory (łączna wartość atrakcyjności kawiarni dla tego klastra)
                double totalScoreValue = cs.beverage() + cs.service() + cs.atmosphere();
                
                cafeAccumulatedScores.putIfAbsent(cafeId, new ArrayList<>());
                cafeAccumulatedScores.get(cafeId).add(totalScoreValue);
            }
        }

        // 5. Wyliczanie średniej atrakcyjności obiektów w klastrze
        Map<Long, Double> cafeFinalRank = new HashMap<>();
        for (Map.Entry<Long, List<Double>> entry : cafeAccumulatedScores.entrySet()) {
            double avg = entry.getValue().stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
            cafeFinalRank.put(entry.getKey(), avg);
        }

        // Odfiltrowujemy kawiarnie, które nasz użytkownik już sam ocenił
        Set<Long> alreadyRatedByTargetUser = userItemMatrix.get(userId).keySet();

        List<Long> recommendedCafeIds = cafeFinalRank.entrySet().stream()
                .filter(entry -> !alreadyRatedByTargetUser.contains(entry.getKey()))
                .sorted((e1, e2) -> Double.compare(e2.getValue(), e1.getValue())) // sortowanie malejąco
                .limit(limit)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        // Mapowanie identyfikatorów na pełne encje Cafe z zachowaniem kolejności rankingu
        return allCafes.stream()
                .filter(cafe -> recommendedCafeIds.contains(cafe.getId()))
                .sorted(Comparator.comparingInt(c -> recommendedCafeIds.indexOf(c.getId())))
                .collect(Collectors.toList());
    }

    /**
     * Trójwymiarowy rdzeń algorytmu K-Means
     */
    private Map<Integer, List<Long>> runMultiDimensionalKMeans(
            List<Long> userIds, 
            Map<Long, Map<Long, CafeScores>> matrix, 
            List<Cafe> allCafes) {
            
        Map<Integer, Map<Long, CafeScores>> centroids = new HashMap<>();
        Random random = new Random(42); // Stały seed dla stabilności wyników

        // Inicjalizacja losowych centroidów z istniejących profili użytkowników
        for (int i = 0; i < K; i++) {
            long randomUserId = userIds.get(random.nextInt(userIds.size()));
            centroids.put(i, new HashMap<>(matrix.get(randomUserId)));
        }

        Map<Integer, List<Long>> clusters = new HashMap<>();

        for (int iter = 0; iter < MAX_ITERATIONS; iter++) {
            clusters.clear();
            for (int i = 0; i < K; i++) clusters.put(i, new ArrayList<>());

            // Krok przypisania: obliczanie wielowymiarowej odległości euklidesowej
            for (Long uId : userIds) {
                int bestCluster = 0;
                double minDistance = Double.MAX_VALUE;

                for (int i = 0; i < K; i++) {
                    double dist = calculateEuclideanDistance3D(matrix.get(uId), centroids.get(i), allCafes);
                    if (dist < minDistance) {
                        minDistance = dist;
                        bestCluster = i;
                    }
                }
                clusters.get(bestCluster).add(uId);
            }

            // Krok aktualizacji: wyliczanie nowego środka ciężkości osobno dla każdej osi (3D)
            for (int i = 0; i < K; i++) {
                List<Long> members = clusters.get(i);
                if (members.isEmpty()) continue;

                Map<Long, CafeScores> newCentroid = new HashMap<>();
                for (Cafe cafe : allCafes) {
                    double bSum = 0, sSum = 0, aSum = 0;
                    int count = 0;
                    
                    for (Long memberId : members) {
                        CafeScores scores = matrix.get(memberId).get(cafe.getId());
                        if (scores != null) {
                            bSum += scores.beverage();
                            sSum += scores.service();
                            aSum += scores.atmosphere();
                            count++;
                        }
                    }
                    if (count > 0) {
                        newCentroid.put(cafe.getId(), new CafeScores(bSum / count, sSum / count, aSum / count));
                    }
                }
                centroids.put(i, newCentroid);
            }
        }
        return clusters;
    }

    /**
     * Matematyczne obliczanie odległości euklidesowej w przestrzeni trójwymiarowej dla wszystkich obiektów
     */
    private double calculateEuclideanDistance3D(Map<Long, CafeScores> userRates, Map<Long, CafeScores> centroidRates, List<Cafe> allCafes) {
        double sum = 0;
        for (Cafe cafe : allCafes) {
            // Dla kawiarni nieocenionych przez danego użytkownika przyjmujemy bezpieczny punkt środkowy (3.0)
            CafeScores u = userRates.getOrDefault(cafe.getId(), new CafeScores(3.0, 3.0, 3.0));
            CafeScores c = centroidRates.getOrDefault(cafe.getId(), new CafeScores(3.0, 3.0, 3.0));
            
            // Suma kwadratów różnic dla trzech niezależnych osi (napoje, obsługa, klimat)
            sum += Math.pow(u.beverage() - c.beverage(), 2);
            sum += Math.pow(u.service() - c.service(), 2);
            sum += Math.pow(u.atmosphere() - c.atmosphere(), 2);
        }
        return Math.sqrt(sum);
    }

    /**
     * Globalny fallback oparty na sumarycznej średniej z trzech składowych ocen
     */
    private List<Cafe> getPopularCafesFallback(List<Cafe> cafes, List<Rate> rates, int limit) {
        Map<Long, List<Double>> globalScores = new HashMap<>();
        for (Rate rate : rates) {
            if (rate.getCafe() == null) continue;
            long cId = rate.getCafe().getId();
            double avg = ((rate.getBeverageScore() != null ? rate.getBeverageScore() : 3) + 
                          (rate.getServiceScore() != null ? rate.getServiceScore() : 3) + 
                          (rate.getAtmosphereScore() != null ? rate.getAtmosphereScore() : 3)) / 3.0;
            globalScores.putIfAbsent(cId, new ArrayList<>());
            globalScores.get(cId).add(avg);
        }

        List<Long> sortedIds = globalScores.entrySet().stream()
                .sorted((e1, e2) -> Double.compare(
                        e2.getValue().stream().mapToDouble(Double::doubleValue).average().orElse(0.0),
                        e1.getValue().stream().mapToDouble(Double::doubleValue).average().orElse(0.0)))
                .limit(limit)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        return cafes.stream()
                .filter(c -> sortedIds.contains(c.getId()))
                .collect(Collectors.toList());
    }
}