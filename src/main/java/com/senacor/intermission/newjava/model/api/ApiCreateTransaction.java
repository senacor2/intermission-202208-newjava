package com.senacor.intermission.newjava.model.api;

import java.time.LocalDateTime;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiCreateTransaction {

    @NotBlank
    @Length(min = 10, max = 24)
    private String receiverIban;

    @NotNull
    @Min(0)
    private Long amountInCents;

    @NotNull
    private LocalDateTime transactionDate;

    private String description;

}
