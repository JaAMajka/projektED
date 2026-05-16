package app.services;

import app.Exceptions.CafeNotFoundException;
import app.Exceptions.RecommendationDoesNotBelongToCafeException;
import app.Exceptions.RecommendationNotFoundException;
import app.Exceptions.UserNotFoundException;
import app.dtos.creating.CreateRecommendationDTO;
import app.dtos.responding.ResponseRecommendationDTO;
import app.dtos.updating.UpdateRecommendationDTO;
import app.mappers.RecommendationMapper;
import app.models.Cafe;
import app.models.Recommendation;
import app.models.User;
import app.repositories.CafeRepository;
import app.repositories.RecommendationRepository;
import app.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RecommendationService {
    private final RecommendationRepository recommendationRepository;
    private final RecommendationMapper recommendationMapper;
    private final CafeRepository cafeRepository;
    private final UserRepository userRepository;


    public ResponseRecommendationDTO getRecommendationDtoById(Long id, Long cafeId, Long userId) {
        return recommendationMapper.toDto(getRecommendationById(id, cafeId, userId));
    }
    private Recommendation getRecommendationById(Long id, Long cafeId, Long userId) {
        Recommendation recommendation = recommendationRepository.findById(id).orElseThrow(() -> new RecommendationNotFoundException("Recommendation not found"));
        cafeRepository.findById(recommendation.getCafe().getId()).orElseThrow(() -> new CafeNotFoundException("Cafe not found"));
        userRepository.findById(recommendation.getUser().getId()).orElseThrow(() -> new UserNotFoundException("User not found"));
        if(recommendation.getCafe().getId().equals(cafeId) && recommendation.getUser().getId().equals(userId)){
            return recommendation;
        } else {
            throw new RecommendationDoesNotBelongToCafeException("Recommendation not found");
        }
    }
    public void deleteRecommendationById(Long id, Long cafeId, Long userId) {
        Recommendation recommendation = getRecommendationById(id, cafeId, userId);
        recommendationRepository.delete(recommendation);
    }
    public Recommendation createRecommendation(CreateRecommendationDTO dto) {
        Recommendation recommendation = recommendationMapper.toEntity(dto);
        Cafe cafe = cafeRepository.findById(dto.cafeId()).orElseThrow(() -> new CafeNotFoundException("Cafe not found"));
        recommendation.setCafe(cafe);
        User user = userRepository.findById(dto.userId()).orElseThrow(() -> new UserNotFoundException("User not found"));
        recommendation.setUser(user);
        return recommendationRepository.save(recommendation);
    }
    public Recommendation updateRecommendation(UpdateRecommendationDTO dto, Long id) {
        Recommendation recommendation = getRecommendationById(id, dto.cafeId(), dto.userId());
        recommendationMapper.updateRecommendationFromDto(dto, recommendation);
        return recommendationRepository.save(recommendation);
    }
}
