package app.security;

import app.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthFilter jwtAuthFilter;
    private final MyUserDetailsService myUserDetailsService;
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/cafes/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/cafes/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/cafes/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/cafes/**/schedules/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/cafes/**/schedules/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/cafes/**/schedules/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/cafes/**/menuItems/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/cafes/**/menuItems/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/cafes/**/menuItems/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .userDetailsService(myUserDetailsService)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}