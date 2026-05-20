package app.dtos.responding;

import app.AvailableSize;

import java.math.BigDecimal;

public record ResponseMenuItemDTO(
        Long id,
        Long cafeId,
        String name,
        BigDecimal price,
        String beverageType,
        Enum<AvailableSize> size,
        Boolean isAppendage,
        Boolean isIced,
        String capacity
) {
}
