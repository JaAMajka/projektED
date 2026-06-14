package app.profiles;

import lombok.Getter;

@Getter
public enum Profile {
    CONNOISSEUR(-0.3, 1.0, 0.0),
    FREELANCER(1.0, 0.0, 0.8),
    STUDENT(-0.3, -0.3, -0.3),
    PERFECTIONIST(0.5, 0.5, 0.5),
    RANDOM_GUY(0.0, 0.0, 0.0);
    private final Double atmosphereBias;
    private final Double beverageBias;
    private final Double serviceBias;
    Profile(Double atmosphereBias, Double beverageBias, Double serviceBias){
        this.atmosphereBias = atmosphereBias;
        this.beverageBias = beverageBias;
        this.serviceBias = serviceBias;
    }

}


