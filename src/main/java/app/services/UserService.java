package app.services;

import app.Exceptions.EmailIsTaken;
import app.Exceptions.PhoneNumberIsTaken;
import app.Exceptions.UserNotFoundException;
import app.dtos.creating.CreateUserDTO;
import app.dtos.responding.ResponseUserDTO;
import app.dtos.updating.UpdateUserDTO;
import app.mappers.UserMapper;
import app.models.User;
import app.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;


    private User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found"));
    }
    public ResponseUserDTO getUserDtoById(Long id) {
        return userMapper.toDto(getUserById(id));
    }
    public void deleteUserById(Long id) {
        User user = getUserById(id);
        userRepository.delete(user);
    }
    public User updateUser(Long userId, UpdateUserDTO dto) {
        User user = getUserById(userId);
        userMapper.updateUserFromDto(dto, user);
        return userRepository.save(user);
    }
    public User createUser(CreateUserDTO dto){
        User user = userMapper.toEntity(dto);
        if (userRepository.findUserByEmail(dto.email()).isPresent()) {
            throw new EmailIsTaken("User with this email already exists");
        }
        if (userRepository.findUserByPhoneNumber(dto.phoneNumber()).isPresent()){
            throw new PhoneNumberIsTaken("User with this phone number already exists");
        }
        return userRepository.save(user);
    }
}
