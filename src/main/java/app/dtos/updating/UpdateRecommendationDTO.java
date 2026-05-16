package app.dtos.updating;

import jakarta.validation.constraints.NotNull;

public record UpdateRecommendationDTO(
        @NotNull Long id,
        @NotNull Long userId,
        @NotNull Long cafeId) {
}
