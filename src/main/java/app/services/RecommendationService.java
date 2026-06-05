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
        log.info("Fetching recommendations for user: {}", userId);
        List<Rate> allRates = rateRepository.findAll();
        List<Cafe> allCafes = cafeRepository.findAll();
        
        if (allRates.isEmpty() || allCafes.isEmpty()) {
            return Collections.emptyList();
        }

        // 1. Budowanie macierzy profilu użytkowników: Map<UserId, Map<CafeId, Double>>
        // Przekształcamy 3 sub-oceny w jedną ogólną średnią ocenę kawiarni
        Map<Long, Map<Long, Double>> userItemMatrix = new HashMap<>();
        for (Rate rate : allRates) {
            if (rate.getAuthor() == null || rate.getCafe() == null) continue;
            
            long uId = rate.getAuthor().getId();
            long cId = rate.getCafe().getId();
            
            // Średnia z trzech składowych
            double avgScore = (rate.getBeverageScore() + rate.getServiceScore() + rate.getAtmosphereScore()) / 3.0;
            
            userItemMatrix.putIfAbsent(uId, new HashMap<>());
            userItemMatrix.get(uId).put(cId, avgScore);
        }

        // Jeśli nasz użytkownik nie ocenił jeszcze niczego, nie wiemy do którego klastra go przypisać.
        // Zwracamy ogólnie najwyżej oceniane kawiarnie w mieście (fallback).
        if (!userItemMatrix.containsKey(userId)) {
            return getPopularCafesFallback(allCafes, allRates, limit);
        }

        // 2. Wykonanie algorytmu K-Means na profilach użytkowników
        List<Long> userIds = new ArrayList<>(userItemMatrix.keySet());
        Map<Integer, List<Long>> clusters = runKMeans(userIds, userItemMatrix, allCafes);

        // 3. Znalezienie klastra, do którego należy docelowy użytkownik
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

        // 4. Agregacja ocen kawiarni wewnątrz wybranego klastra
        List<Long> membersOfCluster = clusters.get(targetClusterId);
        Map<Long, List<Double>> cafeScoresInCluster = new HashMap<>();

        for (Long memberId : membersOfCluster) {
            Map<Long, Double> memberRates = userItemMatrix.get(memberId);
            for (Map.Entry<Long, Double> rateEntry : memberRates.entrySet()) {
                long cafeId = rateEntry.getKey();
                double score = rateEntry.getValue();
                
                cafeScoresInCluster.putIfAbsent(cafeId, new ArrayList<>());
                cafeScoresInCluster.get(cafeId).add(score);
            }
        }

        // 5. Obliczanie średniej dla każdej kawiarni w klastrze i sortowanie od najwyższej
        Map<Long, Double> cafeAverageScores = new HashMap<>();
        for (Map.Entry<Long, List<Double>> entry : cafeScoresInCluster.entrySet()) {
            double avg = entry.getValue().stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
            cafeAverageScores.put(entry.getKey(), avg);
        }

        // Pobieramy ID kawiarni już ocenionych przez naszego użytkownika (nie chcemy polecać tego, co już zna)
        Set<Long> alreadyRatedByTargetUser = userItemMatrix.get(userId).keySet();

        List<Long> recommendedCafeIds = cafeAverageScores.entrySet().stream()
                .filter(entry -> !alreadyRatedByTargetUser.contains(entry.getKey())) // odrzucamy znane kawiarnie
                .sorted((e1, e2) -> Double.compare(e2.getValue(), e1.getValue())) // sortowanie malejąco
                .limit(limit)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        // Mapujemy identyfikatory z powrotem na pełne obiekty encji Cafe
        return allCafes.stream()
                .filter(cafe -> recommendedCafeIds.contains(cafe.getId()))
                .sorted(Comparator.comparingInt(c -> recommendedCafeIds.indexOf(c.getId())))
                .collect(Collectors.toList());
    }

    /**
     * Implementacja rdzenia algorytmu K-Means
     */
    private Map<Integer, List<Long>> runKMeans(List<Long> userIds, Map<Long, Map<Long, Double>> matrix, List<Cafe> allCafes) {
        // Inicjalizacja centroidów (losowe profile bazowe dla K klastrów)
        Map<Integer, Map<Long, Double>> centroids = new HashMap<>();
        Random random = new Random(42); // Seed dla powtarzalności wyników

        for (int i = 0; i < K; i++) {
            long randomUserId = userIds.get(random.nextInt(userIds.size()));
            centroids.put(i, new HashMap<>(matrix.get(randomUserId)));
        }

        Map<Integer, List<Long>> clusters = new HashMap<>();

        // Pętla optymalizacyjna klastrów
        for (int iter = 0; iter < MAX_ITERATIONS; iter++) {
            clusters.clear();
            for (int i = 0; i < K; i++) clusters.put(i, new ArrayList<>());

            // Przypisywanie użytkowników do najbliższego centroidu
            for (Long uId : userIds) {
                int bestCluster = 0;
                double minDistance = Double.MAX_VALUE;

                for (int i = 0; i < K; i++) {
                    double dist = calculateEuclideanDistance(matrix.get(uId), centroids.get(i), allCafes);
                    if (dist < minDistance) {
                        minDistance = dist;
                        bestCluster = i;
                    }
                }
                clusters.get(bestCluster).add(uId);
            }

            // Aktualizacja pozycji centroidów (liczenie średniej pozycji członków klastra)
            for (int i = 0; i < K; i++) {
                List<Long> clusterMembers = clusters.get(i);
                if (clusterMembers.isEmpty()) continue;

                Map<Long, Double> newCentroid = new HashMap<>();
                for (Cafe cafe : allCafes) {
                    double sum = 0;
                    int count = 0;
                    for (Long memberId : clusterMembers) {
                        Double score = matrix.get(memberId).get(cafe.getId());
                        if (score != null) {
                            sum += score;
                            count++;
                        }
                    }
                    if (count > 0) {
                        newCentroid.put(cafe.getId(), sum / count);
                    }
                }
                centroids.put(i, newCentroid);
            }
        }
        return clusters;
    }

    /**
     * Obliczanie odległości euklidesowej między profilami ocen dwóch użytkowników
     */
    private double calculateEuclideanDistance(Map<Long, Double> userRates, Map<Long, Double> centroidRates, List<Cafe> allCafes) {
        double sum = 0;
        for (Cafe cafe : allCafes) {
            // Jeśli użytkownik lub centroid nie ocenili danej kawiarni, przyjmujemy neutralną wartość środkową (3.0)
            double uScore = userRates.getOrDefault(cafe.getId(), 3.0);
            double cScore = centroidRates.getOrDefault(cafe.getId(), 3.0);
            sum += Math.pow(uScore - cScore, 2);
        }
        return Math.sqrt(sum);
    }

    /**
     * Fallback: zwraca globalnie najwyżej oceniane kawiarnie
     */
    private List<Cafe> getPopularCafesFallback(List<Cafe> cafes, List<Rate> rates, int limit) {
        Map<Long, List<Double>> globalScores = new HashMap<>();
        for (Rate rate : rates) {
            if (rate.getCafe() == null) continue;
            long cId = rate.getCafe().getId();
            double avg = (rate.getBeverageScore() + rate.getServiceScore() + rate.getAtmosphereScore()) / 3.0;
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

        return cafes.stream().filter(c -> sortedIds.contains(c.getId())).collect(Collectors.toList());
    }
}
