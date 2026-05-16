package app.dtos.updating;

import app.AvailableSize;
import app.BeverageType;

import java.math.BigDecimal;

public record UpdateMenuItemDTO(Long id, String name, BigDecimal price, Long cafeId, BeverageType beverageType, AvailableSize size, Boolean isAppendage, Boolean isIced, String capacity) {

}
