package app.models;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Table(name = "cafes")
public class Cafe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String address;
    private boolean hasWifi;
    private boolean allowsPets;
    private boolean sellsFood;
    private boolean allowsStudentsDiscounts;
    private boolean isLgbtqFriendly;
    private boolean hasTerrace;
    private boolean allowsIntake;
    private boolean allowsTakeaway;
    private boolean supportsCardPayments;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    @OneToMany(mappedBy = "cafe")
    private List<MenuItem> menuItems;




}
