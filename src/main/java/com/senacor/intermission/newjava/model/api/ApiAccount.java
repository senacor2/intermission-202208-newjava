package com.senacor.intermission.newjava.model.api;

import java.util.UUID;

public record ApiAccount(
    UUID uuid,
    String accountNumber,
    String iban,
    Long balanceInCents) {
}
