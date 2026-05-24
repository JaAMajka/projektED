package app.dtos.updating;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

public record UpdateUserDTO(
        @NotEmpty String name,
        @NotEmpty @Email String email,
        @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$", message = "Password must contain at least 8 characters, one uppercase, one lowercase, one digit and one special character") String password,
        Boolean isStudent, Boolean prefersCardPayment) {
}
