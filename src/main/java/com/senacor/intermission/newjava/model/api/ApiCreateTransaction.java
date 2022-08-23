package com.senacor.intermission.newjava.model.api;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiCreateTransaction {
    private String receiverIban;
    private Long amountInCents;
    private LocalDateTime transactionDate;
    private String description;
}
