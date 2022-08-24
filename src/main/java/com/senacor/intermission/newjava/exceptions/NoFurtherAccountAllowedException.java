package com.senacor.intermission.newjava.exceptions;

import org.springframework.http.HttpStatus;

import java.text.MessageFormat;
import java.util.UUID;

public class NoFurtherAccountAllowedException extends BaseException {
    private final UUID customerUuid;

    public NoFurtherAccountAllowedException(UUID customerUuid) {
        super(HttpStatus.BAD_REQUEST);
        this.customerUuid = customerUuid;
    }

    @Override
    public String getMessage() {
        return MessageFormat.format("Customer {0} cannot have another account!", customerUuid);
    }
}
