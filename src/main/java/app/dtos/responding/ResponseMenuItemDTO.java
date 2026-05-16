package app.dtos.responding;

import java.math.BigDecimal;

public record ResponseMenuItemDTO(
        Long id,
        Long cafeId,
        String name,
        BigDecimal price,
        String beverageType,
        String size,
        Boolean isAppendage,
        Boolean isIced,
        String capacity
) {
}
