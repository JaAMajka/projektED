package app.dtos.updating;

import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;

public record UpdateScheduleDTO(
            @NotNull String dayOfWeek,
            @NotNull LocalTime openingHour,
            @NotNull LocalTime closingHour
) {
}
