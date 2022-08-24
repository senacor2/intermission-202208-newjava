package com.senacor.intermission.newjava.exceptions;

import java.math.BigInteger;
import java.text.MessageFormat;
import java.util.UUID;
import org.springframework.http.HttpStatus;

public class AccountNotFoundException extends BaseException {
    private final String account;

    public AccountNotFoundException(UUID accountUuid) {
        super(HttpStatus.NOT_FOUND);
        this.account = accountUuid.toString();
    }

    public AccountNotFoundException(BigInteger accountId) {
        super(HttpStatus.NOT_FOUND);
        this.account = accountId.toString();
    }

    @Override
    public String getMessage() {
        return MessageFormat.format("Account {0} not found!", account);
    }
}
