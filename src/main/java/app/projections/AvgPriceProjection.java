package app.projections;

import java.math.BigDecimal;

public interface AvgPriceProjection {
    BigDecimal getAvgPrice();
    String getType();
}
