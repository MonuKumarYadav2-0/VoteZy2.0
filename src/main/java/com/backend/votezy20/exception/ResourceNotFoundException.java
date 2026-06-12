package com.backend.votezy20.exception;

public class ResourceNotFoundException
        extends RuntimeException {

    public ResourceNotFoundException(
            String message
    ) {

        super(message);
    }
}