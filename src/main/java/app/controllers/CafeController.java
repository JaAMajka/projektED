package app.controllers;

import app.dtos.creating.CreateCafeDTO;
import app.dtos.filtering.FilterCafeDTO;
import app.dtos.responding.ResponseCafeDTO;
import app.dtos.updating.UpdateCafeDTO;
import app.models.Cafe;
import app.services.CafeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cafes")
public class CafeController {
    private final CafeService cafeService;

    @GetMapping("/{id}")
    public ResponseEntity<ResponseCafeDTO> getCafeById(@PathVariable Long id) {
        ResponseCafeDTO cafeDTO =  cafeService.getCafeDtoById(id);
        return new ResponseEntity<>(cafeDTO, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<ResponseCafeDTO>> getCafesByCriteria(@ModelAttribute FilterCafeDTO criteria) {
        List<ResponseCafeDTO> answer = cafeService.getCafeDtosByFilterCriteria(criteria);
        return new ResponseEntity<>(answer, HttpStatus.OK);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCafeById(@PathVariable Long id) {
        cafeService.deleteCafeById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseCafeDTO> updateCafeById(@PathVariable Long id, @Valid  @RequestBody UpdateCafeDTO dto) {
        cafeService.updateCafe(dto, id);
        return new ResponseEntity<>(cafeService.getCafeDtoById(id), HttpStatus.OK);
    }
    @PostMapping
    public ResponseEntity<ResponseCafeDTO> createCafe(@Valid @RequestBody CreateCafeDTO dto){
        Cafe cafe = cafeService.createCafe(dto);
        return new ResponseEntity<>(cafeService.getCafeDtoById(cafe.getId()), HttpStatus.CREATED);

    }
    @GetMapping
    public ResponseEntity<List<ResponseCafeDTO>> getAllCafes() {
        return new ResponseEntity<>(cafeService.getAllCafeDtos(), HttpStatus.OK);
    }

}
