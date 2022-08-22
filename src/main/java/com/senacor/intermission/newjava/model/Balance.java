package com.senacor.intermission.newjava.model;

import java.math.BigInteger;
import java.time.LocalDateTime;
import javax.persistence.*;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
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

    @OneToOne
    private Account account;

}
