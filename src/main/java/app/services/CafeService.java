package app.services;

import app.Exceptions.CafeAlreadyExistsException;
import app.Exceptions.CafeNotFoundException;
import app.dtos.creating.CreateCafeDTO;
import app.dtos.filtering.FilterCafeDTO;
import app.dtos.responding.ResponseCafeDTO;
import app.dtos.updating.UpdateCafeDTO;
import app.filtering.CafeSpecifications;
import app.mappers.CafeMapper;
import app.models.Cafe;
import app.repositories.CafeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

import static app.filtering.CafeSpecifications.booleanFilter;

@Service
@RequiredArgsConstructor
public class CafeService {
    private final CafeRepository cafeRepository;
    private final CafeMapper cafeMapper;
    private final CafeSpecifications cafeSpecifications;

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
    public Cafe getCafeById(Long id){
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
    public List<ResponseCafeDTO> getCafeDtosByFilterCriteria(FilterCafeDTO filterDto) {
        Specification<Cafe> spec = booleanFilter("hasWifi", filterDto.hasWifi())
                .and(booleanFilter("allowsPets", filterDto.allowsPets()))
                .and(booleanFilter("sellsFood", filterDto.sellsFood()))
                .and(booleanFilter("allowsStudentsDiscounts", filterDto.allowsStudentsDiscounts()))
                .and(booleanFilter("isLgbtqFriendly", filterDto.isLgbtqFriendly()))
                .and(booleanFilter("hasToilet", filterDto.hasToilet()))
                .and(booleanFilter("hasTerrace", filterDto.hasTerrace()))
                .and(booleanFilter("allowsIntake", filterDto.allowsIntake()))
                .and(booleanFilter("allowsTakeaway", filterDto.allowsTakeaway()))
                .and(booleanFilter("supportsCardPayments", filterDto.supportsCardPayments()));

        return cafeRepository.findAll(spec)
                .stream()
                .map(cafeMapper::toDto)
                .toList();
    }




}
