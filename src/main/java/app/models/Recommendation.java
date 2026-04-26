package app.models;


import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
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
