package app.mappers;

import app.dtos.creating.CreateMenuItemDTO;
import app.dtos.creating.CreateScheduleDTO;
import app.dtos.responding.ResponseMenuItemDTO;
import app.dtos.responding.ResponseScheduleDTO;
import app.dtos.updating.UpdateMenuItemDTO;
import app.dtos.updating.UpdateScheduleDTO;
import app.models.MenuItem;
import app.models.Schedule;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ScheduleMapper {
    Schedule toEntity(CreateScheduleDTO dto);
    void updateScheduleFromDto(UpdateScheduleDTO dto, @MappingTarget Schedule schedule);
    ResponseScheduleDTO toDto(Schedule schedule);

}
