package app.models;

import app.AvailableSize;
import app.BeverageType;
import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Getter
@Setter
@Entity
@Table(name = "menu_items")
public class MenuItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private BigDecimal price;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    @ManyToOne
    @JoinColumn(name = "cafe_id")
    private Cafe cafe;
    @Enumerated(EnumType.STRING)
    private BeverageType type;
    @Enumerated(EnumType.STRING)
    private AvailableSize size;
    private Boolean isAppendage;
    private Boolean isIced;
    private String capacity;





}
