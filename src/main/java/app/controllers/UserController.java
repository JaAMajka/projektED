package app.controllers;

import app.dtos.creating.CreateUserDTO;
import app.dtos.responding.ResponseUserDTO;
import app.dtos.updating.UpdateUserDTO;
import app.models.User;
import app.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<ResponseUserDTO> getUserById(@PathVariable Long id) {
        return new ResponseEntity<>(userService.getUserDtoById(id), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable Long id) {
        userService.deleteUserById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseUserDTO> updateUserById(@PathVariable Long id, @RequestBody UpdateUserDTO dto) {
        userService.updateUser(id, dto);
        return new ResponseEntity<>(userService.getUserDtoById(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ResponseUserDTO> createUser(@RequestBody CreateUserDTO dto) {
        User user = userService.createUser(dto);
        return new ResponseEntity<>(userService.getUserDtoById(user.getId()), HttpStatus.CREATED);
    }
}