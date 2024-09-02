package com.sparta.spartaeats.common.exception;

import com.sparta.spartaeats.common.type.ApiResultError;

public class UserException extends Exception {

    private static final long serialVersionUID = 2287905851925635164L;

    private final ApiResultError code;

    public UserException(ApiResultError code) {
        super();
        this.code = code;
    }

    public UserException(String message, Throwable cause, ApiResultError code) {
        super(message, cause);
        this.code = code;
    }

    public UserException(String message, ApiResultError code) {
        super(message);
        this.code = code;
    }

    public UserException(Throwable cause, ApiResultError code) {
        super(cause);
        this.code = code;
    }

    public ApiResultError getCode() {
        return this.code;
    }

}