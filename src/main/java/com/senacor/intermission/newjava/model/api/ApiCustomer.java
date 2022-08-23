package com.senacor.intermission.newjava.model.api;

import java.time.LocalDate;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiCustomer {
    private UUID uuid;
    private String prename;
    private String lastname;
    private LocalDate dateOfBirth;
}
