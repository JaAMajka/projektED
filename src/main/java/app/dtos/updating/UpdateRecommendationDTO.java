package app.dtos.updating;

import jakarta.validation.constraints.NotNull;

public record UpdateRecommendationDTO(
        Integer score
        ) {
}
