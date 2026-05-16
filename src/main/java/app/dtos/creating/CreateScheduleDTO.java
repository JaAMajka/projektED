package app.dtos.creating;


import java.time.LocalTime;

public record CreateScheduleDTO(Long cafeId, String dayOfWeek, LocalTime openingHour, LocalTime closingHour) {
}
