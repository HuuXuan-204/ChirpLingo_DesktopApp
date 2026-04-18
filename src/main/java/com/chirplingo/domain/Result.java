package com.chirplingo.domain;

public class Result {
    private final boolean success;
    private final String message;
    private final String errorCode;

    private Result(boolean success, String message, String errorCode) {
        this.success = success;
        this.message = message;
        this.errorCode = errorCode;
    }

    public static Result success(String message) {
        return new Result(true, message, null);
    }

    public static Result fail(String errorMessage, String errorCode) {
        return new Result(false, errorMessage, errorCode);
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
