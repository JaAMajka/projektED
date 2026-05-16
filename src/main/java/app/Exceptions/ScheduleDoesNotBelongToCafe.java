package app.Exceptions;

public class ScheduleDoesNotBelongToCafe extends RuntimeException {
    public ScheduleDoesNotBelongToCafe(String message) {
        super(message);
    }
}
