package app.dtos.creating;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CreateRecommendationDTO(
        @NotNull Long cafeId,
        @NotNull Long userId,
        @Min(1)
        @Max(5)
        int rating) {
}
