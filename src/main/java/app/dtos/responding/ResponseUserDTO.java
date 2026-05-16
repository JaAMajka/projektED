package app.dtos.responding;

public record ResponseUserDTO(Long id,String username, String email, Boolean isStudent, Boolean prefersCardPayment) {
}
