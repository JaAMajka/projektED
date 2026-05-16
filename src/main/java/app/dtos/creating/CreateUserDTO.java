package app.dtos.creating;

import jakarta.validation.constraints.NotEmpty;
import org.hibernate.validator.constraints.Length;

public record CreateUserDTO(
        @NotEmpty String username,
        @NotEmpty String email,
        @NotEmpty String phoneNumber,
        @NotEmpty @Length(min = 8) String password,
        Boolean isStudent, Boolean prefersCardPayment) {
}
