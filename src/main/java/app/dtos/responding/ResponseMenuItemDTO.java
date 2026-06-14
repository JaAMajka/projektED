package app.dtos.responding;

import app.AvailableSize;

import java.math.BigDecimal;

public record ResponseMenuItemDTO(
        Long id,
        Long cafeId,
        String cafeName,
        String cafeAddress,
        String name,
        BigDecimal price,
        String type,
        Enum<AvailableSize> size,
        Boolean isAppendage,
        Boolean isIced,
        String capacity
) {
}
