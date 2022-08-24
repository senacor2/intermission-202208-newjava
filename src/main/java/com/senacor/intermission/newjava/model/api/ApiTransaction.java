package com.senacor.intermission.newjava.model.api;

import java.time.LocalDateTime;

public record ApiTransaction(
    String senderIban,
    String receiverIban,
    Long amountInCents,
    LocalDateTime transactionDate,
    String description,
    ApiTransactionStatus status) {
}
