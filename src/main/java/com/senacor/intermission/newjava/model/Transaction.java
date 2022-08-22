package com.senacor.intermission.newjava.model;

import com.senacor.intermission.newjava.model.enums.TransactionStatus;
import java.math.BigInteger;
import java.time.LocalDateTime;
import javax.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Entity
public class Transaction extends BaseEntity {

    private BigInteger valueInCents;

    @Enumerated(EnumType.STRING)
    private TransactionStatus status;

    private LocalDateTime transactionTime;

    private String description;

    @ManyToOne
    private Account sender;

    @ManyToOne
    private Account receiver;

}
