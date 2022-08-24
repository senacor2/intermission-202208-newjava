package com.senacor.intermission.newjava.model.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;

public record ApiCreateCustomer(
    @NotBlank @Length(min = 2, max = 200) String prename,
    @NotBlank @Length(min = 2, max = 200) String lastname,
    @NotNull LocalDate dateOfBirth) {
}
