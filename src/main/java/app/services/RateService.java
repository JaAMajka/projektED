package app.services;

import app.repositories.RateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RateService {
    private final RateRepository rateRepository;

}
