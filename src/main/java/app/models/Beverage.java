package app.models;

import app.DrinkType;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Table(name = "beverage_types")
@Entity
public class Beverage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Enum<DrinkType> drinkType;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @OneToMany(mappedBy = "beverage_types")
    private List<MenuItem> menuItemList;








}
