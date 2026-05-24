package app.dtos.responding;

import app.Role;

public record ResponseUserDTO(
        Long id,
        String username,
        String email,
        Enum<Role> role,
        Boolean isStudent,
        Boolean prefersCardPayment
) {
}
