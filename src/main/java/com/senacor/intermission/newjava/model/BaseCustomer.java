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
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING, length = 10)
@DiscriminatorValue("BASE")
public final class BaseCustomer extends Customer {

    @Builder
    public BaseCustomer(String prename, String lastname, LocalDate dateOfBirth, Set<Account> accounts) {
        super(prename, lastname, dateOfBirth, accounts);
        if (this.accounts == null) {
            this.accounts = new HashSet<>();
        }
    }

    public int maxNumberOfAccounts() {
        return 1;
    }
}
