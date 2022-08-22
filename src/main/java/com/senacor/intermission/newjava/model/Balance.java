package com.senacor.intermission.newjava.model;

import java.math.BigInteger;
import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Entity
public class Balance extends BaseEntity {

    private BigInteger valueInCents;
    private LocalDateTime lastUpdate;

    @OneToOne
    private Account account;

}
