package app.Exceptions;

public class PhoneNumberIsTaken extends RuntimeException {
    public PhoneNumberIsTaken(String message) {
        super(message);
    }
}
