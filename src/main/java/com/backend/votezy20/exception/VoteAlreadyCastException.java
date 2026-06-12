package com.backend.votezy20.exception;

public class VoteAlreadyCastException
        extends RuntimeException {

    public VoteAlreadyCastException(
            String message
    ) {

        super(message);
    }
}