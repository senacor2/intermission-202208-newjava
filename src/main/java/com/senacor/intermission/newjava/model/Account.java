package com.senacor.intermission.newjava.model;

import java.math.BigInteger;
import lombok.*;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Table(name = "ACCOUNT")
public class Account extends BaseEntity {

    @Column("acc_number")
    private BigInteger accountNumber;
    private String iban;
    private BigInteger customerId;

}
