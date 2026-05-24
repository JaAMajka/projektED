package app.controllers;
import app.Exceptions.ActionNotAuthorizedException;
import app.Role;
import app.dtos.creating.CreateRateDTO;
import app.dtos.responding.ResponseRateDTO;
import app.dtos.responding.ResponseScheduleDTO;
import app.dtos.updating.UpdateRateDTO;
import app.models.Rate;
import app.security.MyUserDetails;
import app.services.RateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cafes/{cafeId}/rates")
public class RateController {
    private final RateService rateService;

    @GetMapping("/{id}")
    public ResponseEntity<ResponseRateDTO> getRateById(@PathVariable Long id, @PathVariable Long cafeId) {
        ResponseRateDTO rateDto =  rateService.getRateDtoById(id, cafeId);
        return new ResponseEntity<>(rateDto, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRateById(@PathVariable Long id,  @PathVariable Long cafeId) {
        MyUserDetails currentUser = (MyUserDetails) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();



        if (!currentUser.getId().equals(rateService.getRateDtoById(id, cafeId).authorId()) && !currentUser.getAuthorities().contains(Role.ADMIN)) {
            throw new ActionNotAuthorizedException("You are not authorized to delete this rate");
        }
        rateService.deleteRateById(cafeId, id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseRateDTO> updateRateById(@PathVariable Long id, @PathVariable Long cafeId, @RequestBody UpdateRateDTO dto) {
        MyUserDetails currentUser = (MyUserDetails) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();



        if (!currentUser.getId().equals(id) && !currentUser.getAuthorities().contains(Role.ADMIN)) {
            throw new ActionNotAuthorizedException("You are not authorized to delete this rate");
        }
        rateService.updateRate(dto, cafeId, id);
        ResponseRateDTO rateDTO =  rateService.getRateDtoById(cafeId, id);
        return new ResponseEntity<>(rateDTO, HttpStatus.OK);

    }
    @PostMapping
    public ResponseEntity<ResponseRateDTO> createRate(@PathVariable Long cafeId, @RequestBody CreateRateDTO dto){
        Rate rate = rateService.createRate(dto);
        ResponseRateDTO rateDTO =  rateService.getRateDtoById(cafeId, rate.getId());
        return new ResponseEntity<>(rateDTO, HttpStatus.CREATED);
    }
    @GetMapping
    public ResponseEntity<List<ResponseRateDTO>> getRatesByCafeId(@PathVariable Long cafeId) {
        return new ResponseEntity<>(rateService.getRatesByCafeId(cafeId), HttpStatus.OK);
    }
}
