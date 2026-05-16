package app.dtos.responding;

import java.time.LocalDateTime;

public record ResponseRateDTO(
        Long Id,
        Long authorId,
        Long cafeId,
        Integer beverageScore,
        Integer serviceScore,
        Integer atmosphereScore,
        LocalDateTime createdAt) {
}
