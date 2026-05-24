package app.Exceptions;

public class ActionNotAuthorizedException extends RuntimeException {
    public ActionNotAuthorizedException(String message) {
        super(message);
    }
}
