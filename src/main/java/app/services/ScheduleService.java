package app.services;

import app.Exceptions.CafeNotFoundException;
import app.Exceptions.ScheduleDoesNotBelongToCafe;
import app.Exceptions.ScheduleNotFoundException;
import app.dtos.creating.CreateScheduleDTO;
import app.dtos.responding.ResponseScheduleDTO;
import app.dtos.updating.UpdateScheduleDTO;
import app.mappers.ScheduleMapper;
import app.models.Cafe;
import app.models.Schedule;
import app.repositories.CafeRepository;
import app.repositories.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final ScheduleMapper scheduleMapper;
    private final CafeRepository cafeRepository;

    public ResponseScheduleDTO getScheduleDTOById(Long id, Long cafeId) {
        return scheduleMapper.toDto(getScheduleById(id, cafeId));
    }
    public Schedule createSchedule(CreateScheduleDTO dto, Long cafeId) {
        Schedule schedule = scheduleMapper.toEntity(dto);
        Cafe cafe = cafeRepository.findById(cafeId).orElseThrow(() -> new CafeNotFoundException("Cafe not found"));
        schedule.setCafe(cafe);
        return scheduleRepository.save(schedule);
    }
    private Schedule getScheduleById (Long id, Long cafeId) {
        Cafe cafe = cafeRepository.findById(cafeId).orElseThrow(() -> new CafeNotFoundException("Cafe not found"));
        Schedule schedule =  scheduleRepository.findById(id).orElseThrow(() -> new ScheduleNotFoundException("Schedule not found"));
        if (!schedule.getCafe().equals(cafe)){
            throw new ScheduleDoesNotBelongToCafe("This schedule does not belong to this cafe");
        }
        return schedule;
    }

    public void deleteScheduleById(Long cafeId, Long scheduleId) {
        Schedule schedule = getScheduleById(scheduleId, cafeId);
        scheduleRepository.delete(schedule);
    }
    public void updateSchedule(UpdateScheduleDTO dto, Long scheduleId, Long cafeId) {
        Schedule schedule = getScheduleById(scheduleId, cafeId);
        scheduleMapper.updateScheduleFromDto(dto, schedule);
        scheduleRepository.save(schedule);
    }
    public List<ResponseScheduleDTO> getSchedulesByCafeId(Long cafeId){
        cafeRepository.findById(cafeId).orElseThrow(() -> new CafeNotFoundException("Cafe not found."));
        return scheduleRepository.findAllByCafeId(cafeId).
                stream()
                .map(scheduleMapper::toDto)
                .toList();

    }
}
