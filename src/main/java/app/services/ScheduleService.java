package app.services;

import app.Exceptions.CafeNotFoundException;
import app.Exceptions.ScheduleDoesNotBelongToCafe;
import app.Exceptions.ScheduleNotFoundException;
import app.dtos.creating.CreateScheduleDTO;
import app.dtos.responding.ResponseScheduleDTO;
import app.dtos.responding.ResponseUserDTO;
import app.dtos.updating.UpdateScheduleDTO;
import app.mappers.ScheduleMapper;
import app.models.Cafe;
import app.models.Schedule;
import app.repositories.CafeRepository;
import app.repositories.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final ScheduleMapper scheduleMapper;
    private final CafeRepository cafeRepository;

    public ResponseScheduleDTO getUserDtoById(Long id) {
        return scheduleMapper.toDto(getScheduleById(id));
    }
    public Schedule createSchedule(CreateScheduleDTO dto) {
        Schedule schedule = scheduleMapper.toEntity(dto);
        Cafe cafe = cafeRepository.findById(dto.cafeId()).orElseThrow(() -> new CafeNotFoundException("Cafe not found"));
        schedule.setCafe(cafe);
        return scheduleRepository.save(schedule);
    }
    private Schedule getScheduleById (Long id) {
        return scheduleRepository.findById(id).orElseThrow(() -> new ScheduleNotFoundException("Schedule not found"));
    }

    public void deleteScheduleById(Long cafeId, Long scheduleId) {
        Schedule schedule = getScheduleById(scheduleId);
        if (!schedule.getCafe().getId().equals(cafeId)) {
            throw new ScheduleDoesNotBelongToCafe("This schedule does not belong to this cafe");
        }
        scheduleRepository.delete(schedule);
    }
    public Schedule updateSchedule(UpdateScheduleDTO dto) {
        Schedule schedule = getScheduleById(dto.id());
        scheduleMapper.updateScheduleFromDto(dto, schedule);
        return scheduleRepository.save(schedule);
    }
}
