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
    @Mapping(target = "cafe", ignore = true)
    MenuItem toEntity(CreateMenuItemDTO dto);
    void updateMenuItemFromDto(UpdateMenuItemDTO dto, @MappingTarget MenuItem menuItem);
    ResponseMenuItemDTO toDto(MenuItem menuItem);
}
