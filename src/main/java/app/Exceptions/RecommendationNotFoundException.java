package app.Exceptions;

public class RecommendationNotFoundException extends RuntimeException {
    public RecommendationNotFoundException(String message) {
        super(message);
    }
}
