package app.projections;

import app.BeverageType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.math.BigDecimal;

public interface AvgPriceProjection {
    BigDecimal getAvgPrice();
    @Enumerated(EnumType.STRING)
    BeverageType getType();
}
