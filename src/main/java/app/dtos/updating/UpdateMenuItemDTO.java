package app.dtos.updating;

import app.AvailableSize;
import app.BeverageType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record UpdateMenuItemDTO(

        @NotEmpty String name,
        @Positive BigDecimal price,
        @NotNull Long cafeId,
        @NotNull BeverageType beverageType,
        AvailableSize size,
        @NotNull Boolean isAppendage,
        Boolean isIced,
        String capacity) {

}
