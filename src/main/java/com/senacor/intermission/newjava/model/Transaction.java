package com.senacor.intermission.newjava.model;

import com.senacor.intermission.newjava.model.enums.TransactionStatus;
import jakarta.persistence.*;
import java.math.BigInteger;
import java.time.LocalDateTime;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Entity
@Table(name = "TRANSACTION")
public class Transaction extends BaseEntity {

    private BigInteger valueInCents;

    @Enumerated(EnumType.STRING)
    private TransactionStatus status;

    private LocalDateTime transactionTime;

    @Column(name = "descr")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    private Account sender;

    @ManyToOne(fetch = FetchType.LAZY)
    private Account receiver;

}
