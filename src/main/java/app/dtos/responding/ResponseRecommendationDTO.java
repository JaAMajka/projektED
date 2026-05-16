package app.dtos.responding;

import java.time.LocalDateTime;

public record ResponseRecommendationDTO(
        Long id,
        Long userId,
        Long cafeId,
        Integer score,
        LocalDateTime createdAt
) {
}
