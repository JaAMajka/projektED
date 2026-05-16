package app.mappers;

import app.dtos.creating.CreateCafeDTO;
import app.dtos.responding.ResponseCafeDTO;
import app.dtos.updating.UpdateCafeDTO;
import app.models.Cafe;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;


@Mapper(componentModel = "spring")
public interface CafeMapper {
    Cafe toEntity(CreateCafeDTO dto);
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateCafeFromDto(UpdateCafeDTO dto, @MappingTarget Cafe cafe);
    ResponseCafeDTO toDto(Cafe cafe);


}
