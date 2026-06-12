package com.backend.votezy20.exception;

public class ForbiddenException
        extends RuntimeException {

    public ForbiddenException(
            String message
    ) {

        super(message);
    }
}