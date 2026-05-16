package app.dtos.updating;

public record UpdateUserDTO(String username, String email, String password, Boolean isStudent, Boolean prefersCardPayment) {
}
