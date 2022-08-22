package com.senacor.intermission.newjava.model.api;

import java.time.LocalDate;
import java.util.UUID;
import lombok.Data;

@Data
public class ApiCustomer {
    private UUID uuid;
    private String prename;
    private String lastname;
    private LocalDate dateOfBirth;
}
