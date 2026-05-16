package app.dtos.updating;

import app.models.RateId;

public record UpdateRateDTO(RateId rateId, Long authorId, Long cafeId, Long beverageScore, Long serviceScore, Long atmosphereScore) {
}
