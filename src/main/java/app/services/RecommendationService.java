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
import app.models.Recommendation;
import app.models.User;
import app.repositories.CafeRepository;
import app.repositories.RecommendationRepository;
import app.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecommendationService {
    private final RecommendationRepository recommendationRepository;
    private final RecommendationMapper recommendationMapper;
    private final CafeRepository cafeRepository;
    private final UserRepository userRepository;


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
}
