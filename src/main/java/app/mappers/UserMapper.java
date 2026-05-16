package app.mappers;

import app.dtos.creating.CreateUserDTO;
import app.dtos.responding.ResponseUserDTO;
import app.dtos.updating.UpdateUserDTO;
import app.models.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toEntity(CreateUserDTO dto);
    void updateUserFromDto(UpdateUserDTO dto, @MappingTarget User user);
    ResponseUserDTO toDto(User User);

}
