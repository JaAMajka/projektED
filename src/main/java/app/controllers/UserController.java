package app.controllers;

import app.Exceptions.ActionNotAuthorizedException;
import app.Role;
import app.dtos.creating.CreateUserDTO;
import app.dtos.responding.ResponseUserDTO;
import app.dtos.updating.UpdateUserDTO;
import app.models.User;
import app.security.MyUserDetails;
import app.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
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
        MyUserDetails currentUser = (MyUserDetails) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        if (!currentUser.getId().equals(id) && !currentUser.getAuthorities().contains(Role.ADMIN)) {
            throw new ActionNotAuthorizedException("You are not authorized to delete this user");
        }
        userService.deleteUserById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseUserDTO> updateUserById(@PathVariable Long id, @RequestBody UpdateUserDTO dto) {
        MyUserDetails currentUser = (MyUserDetails) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();



        if (!currentUser.getId().equals(id) && !currentUser.getAuthorities().contains(Role.ADMIN)) {
            throw new ActionNotAuthorizedException("You are not authorized to update this user");
        }
        userService.updateUser(id, dto);
        return new ResponseEntity<>(userService.getUserDtoById(id), HttpStatus.OK);
    }

}