package app.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "cafe_read_model")
public class CafeReadModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    @JoinColumn(name = "cafe_id", nullable = false, unique = true)
    private Cafe cafe;
    private BigDecimal avgCoffeePrice;
    private BigDecimal avgWhiteCoffeePrice;
    private BigDecimal avgMatchaPrice;
    private BigDecimal avgBaggedTeaPrice;
    private BigDecimal avgFruityDrinkPrice;
    private BigDecimal avgLeafTeaPrice;
    private BigDecimal avgOtherPrice;
    private Boolean hasIcedItems;
    private BigDecimal avgBeverageScore;
    private BigDecimal avgServiceScore;
    private BigDecimal avgAtmosphereScore;
    private Integer clusterId;
    private BigDecimal stdDevAtmosphere;
    private BigDecimal stdDevBeverage;
    private BigDecimal stdDevService;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

}
