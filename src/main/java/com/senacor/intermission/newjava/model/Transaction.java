package com.senacor.intermission.newjava.model;

import com.senacor.intermission.newjava.model.enums.TransactionStatus;
import java.math.BigInteger;
import java.time.LocalDateTime;
import lombok.*;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Table(name = "TRANSACTION")
public class Transaction extends BaseEntity {

    private BigInteger valueInCents;

    @Column
    private TransactionStatus status;

    private LocalDateTime transactionTime;

    @Column("descr")
    private String description;

    private BigInteger senderId;

    private BigInteger receiverId;

}
