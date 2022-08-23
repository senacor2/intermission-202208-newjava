package com.senacor.intermission.newjava.model.api;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiCreateCustomer {
    private String prename;
    private String lastname;
    private LocalDate dateOfBirth;
}
