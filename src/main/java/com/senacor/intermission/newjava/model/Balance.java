package com.senacor.intermission.newjava.model;

import jakarta.persistence.*;
import java.math.BigInteger;
import java.time.LocalDateTime;
import lombok.*;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Entity
@Table(name = "BALANCE")
@EntityListeners(AuditingEntityListener.class)
public class Balance extends BaseEntity {

    @Builder.Default
    private BigInteger valueInCents = BigInteger.ZERO;

    @LastModifiedDate
    private LocalDateTime lastUpdate;

    @OneToOne(fetch = FetchType.LAZY)
    private Account account;

}
