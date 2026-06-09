package app.Exceptions;

public class PhoneNumberIsTakenException extends RuntimeException {
    public PhoneNumberIsTakenException(String message) {
        super(message);
    }
}
