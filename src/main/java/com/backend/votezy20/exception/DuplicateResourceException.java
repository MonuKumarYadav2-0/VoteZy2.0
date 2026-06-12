package com.backend.votezy20.exception;

public class DuplicateResourceException
        extends RuntimeException {

    public DuplicateResourceException(
            String message
    ) {

        super(message);
    }
}