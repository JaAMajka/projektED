package app.services;

import app.Exceptions.CafeAlreadyExistsException;
import app.Exceptions.CafeNotFoundException;
import app.dtos.creating.CreateCafeDTO;
import app.dtos.updating.UpdateCafeDTO;
import app.mappers.CafeMapper;
import app.models.Cafe;
import app.repositories.CafeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CafeService {
    private final CafeRepository cafeRepository;
    private final CafeMapper cafeMapper;

    public Cafe createCafe(CreateCafeDTO dto){
        Cafe cafe = cafeMapper.toEntity(dto);
        if(cafeRepository.findByNameAndAddress(cafe.getName(), cafe.getAddress()).isPresent()){
            throw new CafeAlreadyExistsException("This cafe already exists");
        }
        return cafeRepository.save(cafe);
    }
    private Cafe getCafeById(Long id){
        return cafeRepository.findById(id).orElseThrow(() -> new CafeNotFoundException("This cafe does not exist"));
    }
    public void deleteCafeById(Long id){
        Cafe cafe = getCafeById(id);
        cafeRepository.delete(cafe);
    }
    public Cafe updateCafe(UpdateCafeDTO dto){
        Cafe cafe = getCafeById(dto.id());
        cafeMapper.updateCafeFromDto(dto, cafe);
        return cafeRepository.save(cafe);
    }




}
