package com.senacor.intermission.newjava.model.api;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiCreateTransaction {

    @NotBlank
    private String receiverIban;

    @NotNull
    @Min(0)
    private Long amountInCents;

    private LocalDateTime transactionDate;

    private String description;

}
