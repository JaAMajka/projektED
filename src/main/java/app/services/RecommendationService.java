package app.services;

import app.Exceptions.RecommendationNotFoundException;
import app.dtos.creating.CreateRecommendationDTO;
import app.mappers.RecommendationMapper;
import app.models.Cafe;
import app.models.Recommendation;
import app.repositories.RecommendationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RecommendationService {
    private final RecommendationRepository recommendationRepository;
    private final RecommendationMapper recommendationMapper;
    private final CafeService cafeService;

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

        return recommendationRepository.save(recommendationMapper.toEntity(dto));
    }
}
