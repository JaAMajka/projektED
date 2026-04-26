package app.models;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    private String phoneNumber;
    private String passwordHash;
    private boolean isStudent;
    private boolean prefersCardPayment;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @OneToMany(mappedBy = "user")
    private List<Rate> rates;
    @OneToMany(mappedBy = "user")
    private List<Recommendation> recommendations;
}