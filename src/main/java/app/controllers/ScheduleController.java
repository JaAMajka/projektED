package app.controllers;
import app.dtos.creating.CreateScheduleDTO;
import app.dtos.responding.ResponseScheduleDTO;
import app.dtos.updating.UpdateScheduleDTO;
import app.models.Schedule;
import app.services.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cafes/{cafeId}/schedules")
public class ScheduleController {
    private final ScheduleService scheduleService;

    @GetMapping("/{id}")
    public ResponseEntity<ResponseScheduleDTO> getScheduleById(@PathVariable Long id, @PathVariable Long cafeId) {
        ResponseScheduleDTO scheduleDTO =  scheduleService.getScheduleDTOById(cafeId, id);
        return new ResponseEntity<>(scheduleDTO, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteScheduleById(@PathVariable Long id,  @PathVariable Long cafeId) {
        scheduleService.deleteScheduleById(cafeId, id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseScheduleDTO> updateScheduleById(@PathVariable Long id, @PathVariable Long cafeId, @RequestBody UpdateScheduleDTO dto) {
        scheduleService.updateSchedule(dto, id, cafeId);
        ResponseScheduleDTO scheduleDTO =  scheduleService.getScheduleDTOById(cafeId, id);
        return new ResponseEntity<>(scheduleDTO, HttpStatus.OK);

    }
    @PostMapping
    public ResponseEntity<ResponseScheduleDTO> createSchedule(@PathVariable Long cafeId, @RequestBody CreateScheduleDTO dto){
        Schedule schedule = scheduleService.createSchedule(dto, cafeId);
        ResponseScheduleDTO scheduleDTO =  scheduleService.getScheduleDTOById(cafeId, schedule.getId());
        return new ResponseEntity<>(scheduleDTO, HttpStatus.CREATED);
    }
    @GetMapping
    public ResponseEntity<List<ResponseScheduleDTO>> getSchedulesByCafeId(@PathVariable Long cafeId) {
        return new ResponseEntity<>(scheduleService.getSchedulesByCafeId(cafeId), HttpStatus.OK);
    }

}
