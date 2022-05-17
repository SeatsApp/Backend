package com.seatapp.controllers.exceptions;

import com.seatapp.exceptions.EntityNotFoundException;
import com.seatapp.services.LoggerService;
import com.seatapp.services.LoggerServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Represents the logger service for logging the errors.
     */
    private final LoggerService loggerService =
            new LoggerServiceImpl(this.getClass());

    /**
     * Is going to throw the right response when an exception is thrown.
     *
     * @param ex is the exception.
     * @return The right response entity.
     */
    @ExceptionHandler({IllegalArgumentException.class,
            EntityNotFoundException.class})
    public ResponseEntity<Object> handleExceptions(final Exception ex) {
        String message = ex.getMessage();
        loggerService.debug(message);
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
