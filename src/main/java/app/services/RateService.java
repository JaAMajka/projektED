package app.services;

import app.Exceptions.CafeNotFoundException;
import app.Exceptions.RateNotFoundException;
import app.Exceptions.UserNotFoundException;
import app.dtos.creating.CreateRateDTO;
import app.dtos.responding.ResponseRateDTO;
import app.dtos.updating.UpdateRateDTO;
import app.mappers.RateMapper;
import app.models.Cafe;
import app.models.Rate;
import app.models.User;
import app.rabbit.EventProducer;
import app.repositories.CafeRepository;
import app.repositories.RateRepository;
import app.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RateService {
    private final RateRepository rateRepository;
    private final RateMapper rateMapper;
    private final CafeRepository cafeRepository;
    private final EventProducer eventProducer;
    private final UserRepository userRepository;


    public ResponseRateDTO getRateDtoById(Long id, Long cafeId) {
        return rateMapper.toDto(getRateById(id, cafeId));
    }
    public Rate createRate(CreateRateDTO dto, Long cafeId, Long userId) {
        Cafe cafe = cafeRepository.findById(cafeId).orElseThrow(() -> new CafeNotFoundException("Cafe not found"));
        Rate rate = rateMapper.toEntity(dto);
        rate.setCafe(cafe);
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User with provided id does not exist."));
        rate.setAuthor(user);
        Rate savedRate =  rateRepository.save(rate);
        eventProducer.sendRateEvent(cafeId);
        return savedRate;
    }
    private Rate getRateById(Long id, Long cafeId) {
        cafeRepository.findById(cafeId).orElseThrow(() -> new CafeNotFoundException("Cafe not found"));
        Rate rate = rateRepository.findById(id).orElseThrow(() -> new RateNotFoundException("Rate not found"));
        if(rate.getCafe().getId().equals(cafeId)){
            return rate;
        } else {
            throw new RateNotFoundException("Rate not found");
        }
    }

    public void deleteRateById(Long id, Long cafeId) {
        Rate rate = getRateById(id, cafeId);
        rateRepository.delete(rate);
    }
    public void updateRate(UpdateRateDTO dto, Long rateId, Long cafeId) {
        Rate rate = getRateById(rateId, cafeId);
        rateMapper.updateRateFromDto(dto, rate);
        rateRepository.save(rate);
        eventProducer.sendMenuItemEvent(cafeId);
    }
    public List<ResponseRateDTO> getRatesByCafeId(Long cafeId){
        cafeRepository.findById(cafeId).orElseThrow(() -> new CafeNotFoundException("Cafe not found."));
        return rateRepository.findAllByCafeId(cafeId).
                stream()
                .map(rateMapper::toDto)
                .toList();

    }
}
