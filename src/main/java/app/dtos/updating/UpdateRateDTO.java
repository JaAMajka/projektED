package app.dtos.updating;

import jakarta.validation.constraints.NotNull;

public record UpdateRateDTO(
        @NotNull Long id,
        @NotNull Long authorId,
        @NotNull Long cafeId,
        Long beverageScore, Long serviceScore, Long atmosphereScore) {
}
