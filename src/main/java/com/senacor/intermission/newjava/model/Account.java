package com.senacor.intermission.newjava.model;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Entity
@Table(name = "ACCOUNT")
@AttributeOverride(
    name = "id",
    column = @Column(name = "id")
)
public class Account extends BaseEntity {

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
    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Transaction> sentTransactions = new HashSet<>();

    @Builder.Default
    @Setter(AccessLevel.PROTECTED)
    @OneToMany(mappedBy = "receiver", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Transaction> receivedTransactions = new HashSet<>();

    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_account")
    @SequenceGenerator(name = "seq_account", sequenceName = "ACCOUNT_NUMBER", allocationSize = 5)
    public BigInteger getId() {
        return super.getId();
    }

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
