package app.dtos.creating;

public record CreateUserDTO(String username, String email, String password, Boolean isStudent, Boolean prefersCardPayment) {
}
