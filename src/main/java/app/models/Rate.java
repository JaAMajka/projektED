package app.models;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "rates")
public class Rate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User author;
    @ManyToOne
    @JoinColumn(name = "cafe_id")
    private Cafe cafe;
    private Integer beverageScore;
    private Integer serviceScore;
    private Integer atmosphereScore;
    @CreationTimestamp
    private LocalDateTime createdAt;
}