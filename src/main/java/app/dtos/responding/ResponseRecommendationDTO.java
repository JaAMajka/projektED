package app.dtos.responding;

import java.time.LocalDateTime;

public record ResponseRecommendationDTO(
        Long id,
        Long userId,
        Long cafeId,
        String userName,
        String cafeName,
        String cafeAddress,
        Integer score,
        LocalDateTime createdAt
) {
}
