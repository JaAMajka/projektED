package app.Exceptions;

public class RecommendationDoesNotBelongToCafeException extends RuntimeException {
    public RecommendationDoesNotBelongToCafeException(String message) {
        super(message);
    }
}
