package com.senacor.intermission.newjava.model;

import com.senacor.intermission.newjava.model.enums.TransactionStatus;
import java.math.BigInteger;
import java.time.LocalDateTime;
import javax.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

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

    @ManyToOne
    private Account sender;

    @ManyToOne
    private Account receiver;

}
