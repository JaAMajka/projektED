package app.models;

import app.Role;
import app.profiles.Profile;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;


import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
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
    @OneToMany(mappedBy = "author")
    private List<Rate> rates;
    @Enumerated(EnumType.STRING)
    private Role role;
    @OneToMany(mappedBy = "user")
    private List<Recommendation> recommendations;
    @Enumerated(EnumType.STRING)
    private Profile profile;
}