package com.senacor.intermission.newjava.model;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Entity
@Table(name = "CUSTOMER")
public class Customer extends BaseEntity {

    private String prename;
    private String lastname;
    private LocalDate dateOfBirth;

    @Builder.Default
    @Setter(AccessLevel.PROTECTED)
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Account> accounts = new HashSet<>();

    public void addAccount(Account account) {
        if (accounts.add(account)) {
            account.setCustomer(this);
        }
    }

}
