package app.dtos.responding;

import app.Role;
import app.profiles.Profile;

public record ResponseUserDTO(
        Long id,
        String name,
        String email,
        Enum<Role> role,
        Enum<Profile> profile,
        Boolean isStudent,
        Boolean prefersCardPayment
) {
}
