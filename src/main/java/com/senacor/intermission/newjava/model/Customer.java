package com.senacor.intermission.newjava.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "CUSTOMER")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class Customer extends BaseEntity {

    protected String prename;
    protected String lastname;
    protected LocalDate dateOfBirth;

    @Setter(AccessLevel.PROTECTED)
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    protected Set<Account> accounts = new HashSet<>();

    public void addAccount(Account account) {
        if (accounts.add(account)) {
            account.setCustomer(this);
        }
    }
}
