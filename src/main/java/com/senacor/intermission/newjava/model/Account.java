package com.senacor.intermission.newjava.model;

import jakarta.persistence.*;
import java.math.BigInteger;
import java.util.HashSet;
import java.util.Set;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Entity
@Table(name = "ACCOUNT")
@SequenceGenerator(name = "seq_account", sequenceName = "ACCOUNT_NUMBER", allocationSize = 5)
public class Account extends BaseEntity {

    @Column(name = "acc_number")
    private BigInteger accountNumber;

    private String iban;

    @ManyToOne
    private Customer customer;

    @OneToOne(
        targetEntity = Balance.class,
        mappedBy = "account",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private Balance balance;

    @Builder.Default
    @Setter(AccessLevel.PROTECTED)
    @OneToMany(
        mappedBy = "sender",
        targetEntity = Transaction.class,
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private Set<Transaction> sentTransactions = new HashSet<>();

    @Builder.Default
    @Setter(AccessLevel.PROTECTED)
    @Getter
    @OneToMany(
        mappedBy = "receiver",
        targetEntity = Transaction.class,
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private Set<Transaction> receivedTransactions = new HashSet<>();

    public void addSentTransaction(Transaction transaction) {
        if (sentTransactions.add(transaction)) {
            transaction.setSender(this);
        }
    }

    public void addReceivedTransaction(Transaction transaction) {
        if (receivedTransactions.add(transaction)) {
            transaction.setReceiver(this);
        }
    }

}
