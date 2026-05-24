package app.models;


import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@Table(name = "recommendations")
@Entity
public class Recommendation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "cafe_id")
    private Cafe cafe;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    private Integer score;
    @CreationTimestamp
    private LocalDateTime createdAt;

}
