package com.senacor.intermission.newjava.model.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiCreateCustomer {

    @NotBlank
    @Length(min = 2, max = 200)
    private String prename;

    @NotBlank
    @Length(min = 2, max = 200)
    private String lastname;

    @NotNull
    private LocalDate dateOfBirth;

}
