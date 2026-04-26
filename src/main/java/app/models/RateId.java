package app.models;

import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;

@Data
@Embeddable
public class RateId implements Serializable {
    private Long userId;
    private Long cafeId;
}