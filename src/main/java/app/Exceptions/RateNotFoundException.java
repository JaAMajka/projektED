package app.Exceptions;

public class RateNotFoundException extends RuntimeException {
    public RateNotFoundException(String message) {
        super(message);
    }
}
