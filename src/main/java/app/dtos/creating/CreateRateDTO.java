package app.dtos.creating;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CreateRateDTO(
        @Min(1)
        @Max(5)
        Integer atmosphereScore,
        @Min(1)
        @Max(5)
        Integer beverageScore,
        @Min(1)
        @Max(5)
        Integer serviceScore
        )
{
}
