package app.Exceptions;

public class EmailIsTaken extends RuntimeException {
    public EmailIsTaken(String message) {
        super(message);
    }
}
