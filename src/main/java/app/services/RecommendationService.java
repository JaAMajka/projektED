package app.services;

import app.Exceptions.RecommendationNotFoundException;
import app.dtos.creating.CreateRecommendationDTO;
import app.mappers.RecommendationMapper;
import app.models.Cafe;
import app.models.Recommendation;
import app.models.Rate;
import app.models.CafeReadModel;
import app.models.GlobalStats;
import app.repositories.RecommendationRepository;
import app.repositories.RateRepository;
import app.repositories.CafeRepository;
import app.repositories.CafeReadModelRepository;
import app.repositories.GlobalStatsReadModelRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RecommendationService {
    
    private final RecommendationRepository recommendationRepository;
    private final RecommendationMapper recommendationMapper;
    private final CafeService cafeService;
    
    private final RateRepository rateRepository;
    private final CafeRepository cafeRepository;
    
    // Nowe reporzytoria z modelami odczytu dodane na obecnym branchu
    private final CafeReadModelRepository cafeReadModelRepository;
    private final GlobalStatsReadModelRepository globalStatsReadModelRepository;

    private static final int K = 4; 
    private static final int MAX_ITERATIONS = 20;

    // Struktura 3D na oceny
    private record CafeScores(double beverage, double service, double atmosphere) {}

    public Recommendation getRecommendationById(Long id) {
        return recommendationRepository.findById(id).orElseThrow(() -> new RecommendationNotFoundException("Recommendation not found"));
    }
    
    public void deleteRecommendationById(Long id) {
        Recommendation recommendation = getRecommendationById(id);
        recommendationRepository.delete(recommendation);
    }
    
    public Recommendation createRecommendation(CreateRecommendationDTO dto) {
        Recommendation recommendation = recommendationMapper.toEntity(dto);
        recommendation.setCafe(cafeService.getCafeById(dto.cafeId()));
        return recommendationRepository.save(recommendation);
    }

   
    public List<Cafe> getRecommendationsForUser(Long userId, int limit) {
        List<Rate> allRates = rateRepository.findAll();
        List<Cafe> allCafes = cafeRepository.findAll();
        List<CafeReadModel> readModels = cafeReadModelRepository.findAll();
        
        if (allRates.isEmpty() || allCafes.isEmpty() || readModels.isEmpty()) {
            return Collections.emptyList();
        }

        Map<Long, CafeReadModel> readModelMap = readModels.stream()
                .collect(Collectors.toMap(m -> m.getCafe().getId(), m -> m, (m1, m2) -> m1));

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

        if (!userItemMatrix.containsKey(userId)) {
            return getPopularCafesFallback(allCafes, readModels, limit);
        }

        List<Long> userIds = new ArrayList<>(userItemMatrix.keySet());
        Map<Integer, List<Long>> clusters = runMultiDimensionalKMeans(userIds, userItemMatrix, allCafes, readModelMap);

        int targetClusterId = -1;
        for (Map.Entry<Integer, List<Long>> entry : clusters.entrySet()) {
            if (entry.getValue().contains(userId)) {
                targetClusterId = entry.getKey();
                break;
            }
        }

        if (targetClusterId == -1) {
            return getPopularCafesFallback(allCafes, readModels, limit);
        }

        List<Long> clusterMembers = clusters.get(targetClusterId);
        Map<Long, List<Double>> cafeAccumulatedScores = new HashMap<>();

        for (Long memberId : clusterMembers) {
            Map<Long, CafeScores> memberRates = userItemMatrix.get(memberId);
            for (Map.Entry<Long, CafeScores> rateEntry : memberRates.entrySet()) {
                long cafeId = rateEntry.getKey();
                CafeScores cs = rateEntry.getValue();
                
                double totalScoreValue = cs.beverage() + cs.service() + cs.atmosphere();
                
                cafeAccumulatedScores.putIfAbsent(cafeId, new ArrayList<>());
                cafeAccumulatedScores.get(cafeId).add(totalScoreValue);
            }
        }

        Map<Long, Double> cafeFinalRank = new HashMap<>();
        for (Map.Entry<Long, List<Double>> entry : cafeAccumulatedScores.entrySet()) {
            double avg = entry.getValue().stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
            cafeFinalRank.put(entry.getKey(), avg);
        }

        Set<Long> alreadyRatedByTargetUser = userItemMatrix.get(userId).keySet();

        List<Long> recommendedCafeIds = cafeFinalRank.entrySet().stream()
                .filter(entry -> !alreadyRatedByTargetUser.contains(entry.getKey()))
                .sorted((e1, e2) -> Double.compare(e2.getValue(), e1.getValue()))
                .limit(limit)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        return allCafes.stream()
                .filter(cafe -> recommendedCafeIds.contains(cafe.getId()))
                .sorted(Comparator.comparingInt(c -> recommendedCafeIds.indexOf(c.getId())))
                .collect(Collectors.toList());
    }

    private Map<Integer, List<Long>> runMultiDimensionalKMeans(
            List<Long> userIds, 
            Map<Long, Map<Long, CafeScores>> matrix, 
            List<Cafe> allCafes,
            Map<Long, CafeReadModel> readModelMap) {
            
        Map<Integer, Map<Long, CafeScores>> centroids = new HashMap<>();
        Random random = new Random(42);

        for (int i = 0; i < K; i++) {
            long randomUserId = userIds.get(random.nextInt(userIds.size()));
            centroids.put(i, new HashMap<>(matrix.get(randomUserId)));
        }

        Map<Integer, List<Long>> clusters = new HashMap<>();

        for (int iter = 0; iter < MAX_ITERATIONS; iter++) {
            clusters.clear();
            for (int i = 0; i < K; i++) clusters.put(i, new ArrayList<>());

            for (Long uId : userIds) {
                int bestCluster = 0;
                double minDistance = Double.MAX_VALUE;

                for (int i = 0; i < K; i++) {
                    double dist = calculateEuclideanDistance3D(matrix.get(uId), centroids.get(i), allCafes, readModelMap);
                    if (dist < minDistance) {
                        minDistance = dist;
                        bestCluster = i;
                    }
                }
                clusters.get(bestCluster).add(uId);
            }

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

   
    private double calculateEuclideanDistance3D(
            Map<Long, CafeScores> userRates, 
            Map<Long, CafeScores> centroidRates, 
            List<Cafe> allCafes,
            Map<Long, CafeReadModel> readModelMap) {
            
        double sum = 0;
        for (Cafe cafe : allCafes) {
            CafeReadModel readModel = readModelMap.get(cafe.getId());
            double defaultB = (readModel != null && readModel.getAvgBeverageScore() != null) ? readModel.getAvgBeverageScore().doubleValue() : 3.5;
            double defaultS = (readModel != null && readModel.getAvgServiceScore() != null) ? readModel.getAvgServiceScore().doubleValue() : 3.5;
            double defaultA = (readModel != null && readModel.getAvgAtmosphereScore() != null) ? readModel.getAvgAtmosphereScore().doubleValue() : 3.5;

            CafeScores u = userRates.getOrDefault(cafe.getId(), new CafeScores(defaultB, defaultS, defaultA));
            CafeScores c = centroidRates.getOrDefault(cafe.getId(), new CafeScores(defaultB, defaultS, defaultA));
            
            sum += Math.pow(u.beverage() - c.beverage(), 2);
            sum += Math.pow(u.service() - c.service(), 2);
            sum += Math.pow(u.atmosphere() - c.atmosphere(), 2);
        }
        return Math.sqrt(sum);
    }

    private List<Cafe> getPopularCafesFallback(List<Cafe> cafes, List<CafeReadModel> readModels, int limit) {
        List<Long> sortedIds = readModels.stream()
                .sorted((m1, m2) -> {
                    double score1 = (m1.getAvgBeverageScore().doubleValue() + m1.getAvgServiceScore().doubleValue() + m1.getAvgAtmosphereScore().doubleValue()) / 3.0;
                    double score2 = (m2.getAvgBeverageScore().doubleValue() + m2.getAvgServiceScore().doubleValue() + m2.getAvgAtmosphereScore().doubleValue()) / 3.0;
                    return Double.compare(score2, score1);
                })
                .limit(limit)
                .map(m -> m.getCafe().getId())
                .collect(Collectors.toList());

        return cafes.stream()
                .filter(c -> sortedIds.contains(c.getId()))
                .collect(Collectors.toList());
    }
}