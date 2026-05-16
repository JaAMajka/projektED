package app.dtos.creating;

public record CreateRecommendationDTO(Long cafeId, Long userId, String comment, int rating) {
}
