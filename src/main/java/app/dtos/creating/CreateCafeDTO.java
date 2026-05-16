package app.dtos.creating;

import java.time.LocalDateTime;

public record CreateCafeDTO(String name, String address, LocalDateTime dateTime) {
}
