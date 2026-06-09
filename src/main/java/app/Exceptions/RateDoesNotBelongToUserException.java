package app.Exceptions;

public class RateDoesNotBelongToUserException extends RuntimeException {
    public RateDoesNotBelongToUserException(String message) {
        super(message);
    }
}
