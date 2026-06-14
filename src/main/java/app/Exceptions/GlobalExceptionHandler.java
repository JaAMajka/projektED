package app.Exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({
            CafeNotFoundException.class,
            UserNotFoundException.class,
            MenuItemNotFoundException.class,
            ScheduleNotFoundException.class,
            RateNotFoundException.class,
            RecommendationNotFoundException.class
    })
    public ResponseEntity<ErrorResponse> handleNotFoundExceptions(RuntimeException ex) {
        ErrorResponse ErrorResponse = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                "Not Found", ex.getMessage());
        return new ResponseEntity<>(ErrorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(app.Exceptions.ItemAlreadyBelongsToCafeException.class)
    public ResponseEntity<ErrorResponse> handleBadRequestExceptions(RuntimeException ex) {
        ErrorResponse ErrorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                ex.getMessage()
        );
        return new ResponseEntity<>(ErrorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({
            RateDoesNotBelongToUserException.class,
            ItemDoesNotBelongToCafeException.class,
            ScheduleDoesNotBelongToCafeException.class,
            UnauthorizedActionException.class,
            RecommendationDoesNotBelongToUserException.class
    })
    public ResponseEntity<ErrorResponse> handleForbiddenException(Exception ex) {
        ErrorResponse ErrorResponse = new ErrorResponse(
                HttpStatus.FORBIDDEN.value(),
                "Forbidden",
                "You are not authorized to perform this action"
        );
        return new ResponseEntity<>(ErrorResponse, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(Exception ex) {
        ErrorResponse ErrorResponse = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error",
                "An unexpected error occurred"
        );
        return new ResponseEntity<>(ErrorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @ExceptionHandler({
            EmailIsTakenException.class,
            CafeAlreadyExistsException.class,
            PhoneNumberIsTakenException.class
    }
    )
    public ResponseEntity<ErrorResponse> handleConflictExceptions(Exception ex) {
        ErrorResponse ErrorResponse = new ErrorResponse(
                HttpStatus.CONFLICT.value(),
                "Conflict",
                ex.getMessage()
        );
        return new ResponseEntity<>(ErrorResponse, HttpStatus.CONFLICT);
    }
}