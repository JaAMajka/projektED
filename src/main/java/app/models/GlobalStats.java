package app.models;

import app.BeverageType;
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
    @Enumerated(EnumType.STRING)
    @Column(unique = true, nullable = false)
    private BeverageType beverageType;
    private BigDecimal avgPrice;
    private BigDecimal avgBeverageScore;
    private BigDecimal avgServiceScore;
    private BigDecimal avgAtmosphereScore;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}