package com.sparta.spartaeats.common.exception;

import com.sparta.spartaeats.common.type.ApiResultError;

public class TokenException extends Exception {

    private static final long serialVersionUID = 2287905851925635164L;

    private final ApiResultError code;

    public TokenException(ApiResultError code) {
        super();
        this.code = code;
    }

    public TokenException(String message, Throwable cause, ApiResultError code) {
        super(message, cause);
        this.code = code;
    }

    public TokenException(String message, ApiResultError code) {
        super(message);
        this.code = code;
    }

    public TokenException(Throwable cause, ApiResultError code) {
        super(cause);
        this.code = code;
    }

    public ApiResultError getCode() {
        return this.code;
    }

}