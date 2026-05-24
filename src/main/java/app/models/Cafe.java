package app.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "cafes")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cafe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String address;
    private Boolean hasWifi;
    private Boolean allowsPets;
    private Boolean sellsFood;
    private Boolean allowsStudentsDiscounts;
    private Boolean isLgbtqFriendly;
    private Boolean hasTerrace;
    private Boolean allowsIntake;
    private Boolean allowsTakeaway;
    private Boolean supportsCardPayments;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    @OneToMany(mappedBy = "cafe")
    private List<MenuItem> menuItems;
    @OneToMany(mappedBy = "cafe")
    private List<Schedule> schedules;




}
