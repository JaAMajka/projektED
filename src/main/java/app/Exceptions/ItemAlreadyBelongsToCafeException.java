package app.Exceptions;

public class ItemAlreadyBelongsToCafeException extends RuntimeException {
    public ItemAlreadyBelongsToCafeException(String message) {
        super(message);
    }
}
