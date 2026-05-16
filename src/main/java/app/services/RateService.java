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
import app.repositories.CafeRepository;
import app.repositories.RateRepository;
import app.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RateService {
    private final RateRepository rateRepository;
    private final RateMapper rateMapper;
    private final UserRepository userRepository;
    private final CafeRepository cafeRepository;


    public ResponseRateDTO getRateDtoById(Long id, Long cafeId, Long userId) {
        return rateMapper.toDto(getRateById(id, cafeId, userId));
    }
    public Rate createRate(CreateRateDTO dto) {
        Rate rate = rateMapper.toEntity(dto);
        return rateRepository.save(rate);
    }
    private Rate getRateById(Long id, Long cafeId, Long userId) {
        cafeRepository.findById(cafeId).orElseThrow(() -> new CafeNotFoundException("Cafe not found"));
        userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found"));
        Rate rate = rateRepository.findById(id).orElseThrow(() -> new RateNotFoundException("Rate not found"));
        if(rate.getAuthor().getId().equals(userId) && rate.getCafe().getId().equals(cafeId)){
            return rate;
        } else {
            throw new RateNotFoundException("Rate not found");
        }
    }

    public void deleteRateById(Long id) {
        Rate rate = rateRepository.findById(id).orElseThrow(() -> new RateNotFoundException("Rate not found"));
        rateRepository.delete(rate);
    }
    public Rate updateRate(UpdateRateDTO dto) {
        Rate rate = getRateById(dto.id(), dto.cafeId(), dto.authorId());
        rateMapper.updateRateFromDto(dto, rate);
        return rateRepository.save(rate);
    }
}
