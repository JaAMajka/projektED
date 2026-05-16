package app.dtos.creating;
import app.AvailableSize;
import app.BeverageType;
import java.math.BigDecimal;

public record CreateMenuItemDTO(String name, BigDecimal price, Long cafeId, BeverageType beverageType, AvailableSize size, Boolean isAppendage, Boolean isIced, String capacity) {
}
