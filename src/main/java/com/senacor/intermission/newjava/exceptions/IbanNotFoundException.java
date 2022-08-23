package com.senacor.intermission.newjava.exceptions;

import java.text.MessageFormat;
import org.springframework.http.HttpStatus;

public class IbanNotFoundException extends BaseException {
    private final String iban;

    public IbanNotFoundException(String iban) {
        super(HttpStatus.NOT_FOUND);
        this.iban = iban;
    }

    @Override
    public String getMessage() {
        return MessageFormat.format("No account with iban {0} found!", iban);
    }
}
