package app.projections;

import java.math.BigDecimal;

public interface AvgRateProjection {
    BigDecimal getAvgAtmosphereScore();
    BigDecimal getAvgBeverageScore();
    BigDecimal getAvgServiceScore();
}