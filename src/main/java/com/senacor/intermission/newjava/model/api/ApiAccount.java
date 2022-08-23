package com.senacor.intermission.newjava.model.api;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class ApiAccount {
    private UUID uuid;
    private String iban;
    private Long balanceInCents;
}
