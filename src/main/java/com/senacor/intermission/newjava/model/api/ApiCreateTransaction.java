package com.senacor.intermission.newjava.model.api;

import java.time.LocalDateTime;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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
