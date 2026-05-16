package app.dtos.creating;
import app.AvailableSize;
import app.BeverageType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record CreateMenuItemDTO(
        @NotNull String name,
        @NotNull @Positive BigDecimal price,
        @NotNull Long cafeId,
        @NotNull BeverageType beverageType,
        AvailableSize size,
        @NotNull Boolean isAppendage,
        Boolean isIced,
        String capacity) {
}
