package app.dtos.creating;


import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;

public record CreateScheduleDTO(
        @NotNull Long cafeId,
        @NotNull String dayOfWeek,
        @NotNull LocalTime openingHour,
        @NotNull LocalTime closingHour) {
}
