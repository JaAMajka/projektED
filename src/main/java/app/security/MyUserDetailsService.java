package app.security;

import app.Exceptions.UserNotFoundException;
import app.models.User;
import app.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MyUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) {
        User user = userRepository.findUserByEmail(email).orElseThrow(() ->
                new UserNotFoundException("User not found with email: " + email));
        return new MyUserDetails(user);
    }
}
