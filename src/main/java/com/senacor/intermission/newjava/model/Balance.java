package com.senacor.intermission.newjava.model;

import java.math.BigInteger;
import java.time.LocalDateTime;
import lombok.*;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Table(name = "BALANCE")
public class Balance extends BaseEntity {

    @Builder.Default
    private BigInteger valueInCents = BigInteger.ZERO;

    @LastModifiedDate
    private LocalDateTime lastUpdate;

    private BigInteger accountId;

}
