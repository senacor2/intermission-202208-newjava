package com.senacor.intermission.newjava.exceptions;

import org.springframework.http.HttpStatus;

import java.text.MessageFormat;
import java.util.UUID;

public class CustomerNotFoundException extends BaseException {
    private final UUID customerUuid;

    public CustomerNotFoundException(UUID customerUuid) {
        super(HttpStatus.NOT_FOUND);
        this.customerUuid = customerUuid;
    }

    @Override
    public String getMessage() {
        return MessageFormat.format("Customer {0} not found!", customerUuid);
    }
}
