package app.Exceptions;

public class ItemDoesNotBelongToCafeException extends RuntimeException {
    public ItemDoesNotBelongToCafeException(String message) {
        super(message);
    }
}
