package app.dtos.responding;

import java.time.LocalDateTime;
public record ResponseRateDTO(
        Long id,
        Long authorId,
        Long cafeId,
        String authorName,
        String cafeName,
        String cafeAddress,
        Integer beverageScore,
        Integer serviceScore,
        Integer atmosphereScore,
        LocalDateTime createdAt) {
}
