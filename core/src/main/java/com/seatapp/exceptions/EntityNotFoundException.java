package com.seatapp.exceptions;

import java.io.Serial;

public class EntityNotFoundException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * An entity not found exception.
     *
     * @param message the message given in the exception
     */
    public EntityNotFoundException(final String message) {
        super(message);
    }
}
