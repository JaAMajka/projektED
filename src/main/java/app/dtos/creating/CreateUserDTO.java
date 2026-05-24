package app.dtos.creating;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

public record CreateUserDTO(
        @NotEmpty String name,
        @NotEmpty String email,
        @NotEmpty String phoneNumber,
        @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$", message = "Password must contain at least 8 characters, one uppercase, one lowercase, one digit and one special character") String password,
        Boolean isStudent, Boolean prefersCardPayment) {
}
