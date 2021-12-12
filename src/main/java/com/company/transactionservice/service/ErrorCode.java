package com.company.transactionservice.service;

public enum ErrorCode {
    INSUFFICIENT_FOUNDS( "00456"),
    EXCEED_LIMIT( "00457"),
    NOT_SUPPORTED( "00458"),
    NOT_FOUND("00404");

    private final String errorCode;

    ErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
