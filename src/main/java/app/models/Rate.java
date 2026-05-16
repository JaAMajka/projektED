package app.models;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "rates")
public class Rate {
    @EmbeddedId
    private RateId id;
    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User author;
    @ManyToOne
    @MapsId("cafeId")
    @JoinColumn(name = "cafe_id")
    private Cafe cafe;
    private Integer beverageScore;
    private Integer serviceScore;
    private Integer atmosphereScore;
    @CreationTimestamp
    private LocalDateTime createdAt;
}