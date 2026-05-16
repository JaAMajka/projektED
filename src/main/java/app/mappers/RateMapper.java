package app.mappers;
import app.dtos.creating.CreateRateDTO;
import app.dtos.responding.ResponseRateDTO;
import app.dtos.updating.UpdateRateDTO;
import app.models.MenuItem;
import app.models.Rate;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface RateMapper {
    MenuItem toEntity(CreateRateDTO dto);
    void updateRateFromDto(UpdateRateDTO dto, @MappingTarget Rate rate);
    ResponseRateDTO toDto(Rate menuItem);

}
