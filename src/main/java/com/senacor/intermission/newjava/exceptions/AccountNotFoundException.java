package com.senacor.intermission.newjava.exceptions;

import java.text.MessageFormat;
import java.util.UUID;
import org.springframework.http.HttpStatus;

public class AccountNotFoundException extends BaseException {
    private final UUID accountUuid;

    public AccountNotFoundException(UUID accountUuid) {
        super(HttpStatus.NOT_FOUND);
        this.accountUuid = accountUuid;
    }

    @Override
    public String getMessage() {
        return MessageFormat.format("Account {0} not found!", accountUuid);
    }
}
