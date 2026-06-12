package com.backend.votezy20.exception;

public class BadRequestException
        extends RuntimeException {

    public BadRequestException(
            String message
    ) {

        super(message);
    }
}