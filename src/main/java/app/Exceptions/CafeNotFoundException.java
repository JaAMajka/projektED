package app.Exceptions;

public class CafeNotFoundException extends RuntimeException {
    public CafeNotFoundException(String message) {
        super(message);
    }
}
