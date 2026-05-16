package app.dtos.updating;

import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;

public record UpdateScheduleDTO(
            @NotNull Long id,
            @NotNull Long cafeId,
            @NotNull String dayOfWeek,
            @NotNull LocalTime openingHour,
            @NotNull LocalTime closingHour
) {
}
