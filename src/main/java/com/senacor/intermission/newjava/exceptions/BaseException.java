package com.senacor.intermission.newjava.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;

public abstract class BaseException extends HttpStatusCodeException {
    protected BaseException(HttpStatus statusCode) {
        super(statusCode);
    }

    @Override
    public abstract String getMessage();
}
