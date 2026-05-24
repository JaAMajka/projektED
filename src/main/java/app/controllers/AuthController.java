package app.controllers;

import app.dtos.LoginDTO;
import app.dtos.creating.CreateUserDTO;
import app.models.User;
import app.security.JwtUtil;
import app.security.MyUserDetails;
import app.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtUtil jwtService;


    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody CreateUserDTO dto) {
        User user = userService.createUser(dto);
        return new ResponseEntity<>("User with id " + user.getId() + " created successfully", HttpStatus.CREATED);
    }
    @PostMapping("/login")
    public String login(@RequestBody @Valid LoginDTO dto) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.email(), dto.password())
        );
        MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
        return jwtService.generateToken(userDetails);



    }

}
