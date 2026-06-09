package app.models;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@Getter
@Setter
@Entity
@Table(name = "global_stats")
public class GlobalStats {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private BigDecimal avgPricePureCoffee;
    private BigDecimal avgPriceCoffeeDrinks;
    private BigDecimal avgPriceMatcha;
    private BigDecimal avgPriceBaggedTea;
    private BigDecimal avgPriceFruityDrink;
    private BigDecimal avgPriceLeafTea;
    private BigDecimal avgPriceOther;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}