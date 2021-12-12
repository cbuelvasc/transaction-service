package com.company.transactionservice.service.exception;

public class ExceedLimitException extends RuntimeException {

    public ExceedLimitException() {
        super();
    }

    public ExceedLimitException(String message) {
        super(message);
    }

    public ExceedLimitException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExceedLimitException(Throwable cause) {
        super(cause);
    }
}
