package app.Exceptions;

public class CafeAlreadyExistsException extends RuntimeException {
    public CafeAlreadyExistsException(String message) {
        super(message);
    }
}
