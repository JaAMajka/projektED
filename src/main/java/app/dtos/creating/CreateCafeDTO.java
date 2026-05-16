package app.dtos.creating;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record CreateCafeDTO(
        @NotNull String name,
        @NotNull String address,
        @NotNull LocalDateTime dateTime) {
}
