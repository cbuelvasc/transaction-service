package com.company.transactionservice.service.exception;

public class NotSupportedCurrencyException extends RuntimeException {

  public NotSupportedCurrencyException() {
    super();
  }

  public NotSupportedCurrencyException(String message) {
    super(message);
  }

  public NotSupportedCurrencyException(String message, Throwable cause) {
    super(message, cause);
  }

  public NotSupportedCurrencyException(Throwable cause) {
    super(cause);
  }
}
