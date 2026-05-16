package app.dtos.responding;

import app.models.RateId;

public record ResponseRateDTO(RateId rateId, Long authorId, Long cafeId, Long beverageScore, Long serviceScore, Long atmosphereScore) {
}
