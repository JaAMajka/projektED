package app.projections;

import java.math.BigDecimal;

public interface StatsRateProjection {
    BigDecimal getAvgAtmosphereScore();
    BigDecimal getAvgBeverageScore();
    BigDecimal getAvgServiceScore();
    BigDecimal getStdDevAtmosphere();
    BigDecimal getStdDevBeverage();
    BigDecimal getStdDevService();
}