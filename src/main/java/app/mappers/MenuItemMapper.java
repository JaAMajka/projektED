package app.mappers;

import app.dtos.creating.CreateMenuItemDTO;
import app.dtos.responding.ResponseMenuItemDTO;
import app.dtos.updating.UpdateMenuItemDTO;
import app.models.MenuItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;


@Mapper(componentModel = "spring")
public interface MenuItemMapper {
    MenuItem toEntity(CreateMenuItemDTO dto);
    void updateMenuItemFromDto(UpdateMenuItemDTO dto, @MappingTarget MenuItem menuItem);
    @Mapping(source = "cafe.name", target = "cafeName")
    @Mapping(source = "cafe.address", target = "cafeAddress")
    @Mapping(source = "type", target = "type")
    ResponseMenuItemDTO toDto(MenuItem menuItem);
}
