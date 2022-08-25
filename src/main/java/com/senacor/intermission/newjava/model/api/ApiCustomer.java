package com.senacor.intermission.newjava.model.api;

import java.time.LocalDate;
import java.util.UUID;

public record ApiCustomer(
    UUID uuid,
    String prename,
    String lastname,
    LocalDate dateOfBirth,
    ApiCustomerType type) {
}
