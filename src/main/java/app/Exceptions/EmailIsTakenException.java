package app.Exceptions;

public class EmailIsTakenException extends RuntimeException {
    public EmailIsTakenException(String message) {
        super(message);
    }
}
