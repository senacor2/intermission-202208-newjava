package com.senacor.intermission.newjava.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public abstract class BaseException extends ResponseStatusException {
    protected BaseException(HttpStatus statusCode) {
        super(statusCode);
    }

    protected BaseException(HttpStatus statusCode, Throwable cause) {
        super(statusCode, null, cause);
    }

    @Override
    public abstract String getMessage();
}
