package app.dtos.responding;

import app.Weekday;

import java.time.LocalTime;

public record ResponseScheduleDTO(
        Long id,
        Long cafeId,
        Weekday dayOfWeek,
        LocalTime openingHour,
        LocalTime closingHour) {
}
