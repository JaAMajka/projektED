package app.services;

import app.Exceptions.CafeAlreadyExistsException;
import app.Exceptions.CafeNotFoundException;
import app.dtos.creating.CreateCafeDTO;
import app.dtos.responding.ResponseCafeDTO;
import app.dtos.updating.UpdateCafeDTO;
import app.mappers.CafeMapper;
import app.models.Cafe;
import app.repositories.CafeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public ResponseCafeDTO getCafeDtoById(Long id){
        return cafeMapper.toDto(getCafeById(id));
    }
    private Cafe getCafeById(Long id){
        return cafeRepository.findById(id).orElseThrow(() -> new CafeNotFoundException("This cafe does not exist"));
    }
    public void deleteCafeById(Long id){
        Cafe cafe = getCafeById(id);
        cafeRepository.delete(cafe);
    }
    public void updateCafe(UpdateCafeDTO dto, Long id){
        Cafe cafe = getCafeById(id);
        cafeMapper.updateCafeFromDto(dto, cafe);
        cafeRepository.save(cafe);

    }
    public List<ResponseCafeDTO> getAllCafeDtos(){
        return cafeRepository
                .findAll()
                .stream()
                .map(cafeMapper::toDto)
                .toList();
    }




}
