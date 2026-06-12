package com.backend.votezy20.exception;

public class ElectionClosedException
        extends RuntimeException {

    public ElectionClosedException(
            String message
    ) {

        super(message);
    }
}