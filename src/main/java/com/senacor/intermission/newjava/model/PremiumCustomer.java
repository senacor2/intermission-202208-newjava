package com.senacor.intermission.newjava.model;

import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING, length = 20)
@DiscriminatorValue("PREMIUM")
public final class PremiumCustomer extends Customer {

    @Builder
    public PremiumCustomer(String prename, String lastname, LocalDate dateOfBirth, Set<Account> accounts) {
        super(prename, lastname, dateOfBirth, accounts);
        if (accounts == null) {
            accounts = new HashSet<>();
        }
    }
}
