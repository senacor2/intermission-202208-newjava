package com.senacor.intermission.newjava.model.api;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record ApiCreateTransaction(
    @NotBlank String receiverIban,
    @NotNull @Min(0) Long amountInCents,
    LocalDateTime transactionDate,
    String description) {
}
