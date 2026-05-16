package app.dtos.updating;

import jakarta.validation.constraints.NotEmpty;
import org.hibernate.validator.constraints.Length;

public record UpdateUserDTO(
        @NotEmpty String name,
        @NotEmpty String email,
        @NotEmpty @Length(min = 8) String password,
        Boolean isStudent, Boolean prefersCardPayment) {
}
