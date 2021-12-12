package com.company.transactionservice.service.exception;

public class InsufficientFoundsException extends RuntimeException {

    public InsufficientFoundsException() {
        super();
    }

    public InsufficientFoundsException(String message) {
        super(message);
    }

    public InsufficientFoundsException(String message, Throwable cause) {
        super(message, cause);
    }

    public InsufficientFoundsException(Throwable cause) {
        super(cause);
    }
}
