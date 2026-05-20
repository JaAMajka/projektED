package app.Exceptions;

public class RecommendationDoesNotBelongToUserException extends RuntimeException {
    public RecommendationDoesNotBelongToUserException(String message) {
        super(message);
    }
}
