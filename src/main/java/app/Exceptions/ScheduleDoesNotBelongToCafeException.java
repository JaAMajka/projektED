package app.Exceptions;

public class ScheduleDoesNotBelongToCafeException extends RuntimeException {
    public ScheduleDoesNotBelongToCafeException(String message) {
        super(message);
    }
}
