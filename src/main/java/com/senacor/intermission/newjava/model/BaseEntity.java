package com.senacor.intermission.newjava.model;

import java.math.BigInteger;
import java.util.UUID;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import lombok.*;
import org.springframework.data.domain.Persistable;

@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@MappedSuperclass
public class BaseEntity implements Persistable<BigInteger> {

    @Setter(AccessLevel.PROTECTED)
    private BigInteger id;
    private UUID uuid = UUID.randomUUID();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public BigInteger getId() {
        return this.id;
    }

    @Override
    public boolean isNew() {
        return id == null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BaseEntity)) {
            return false;
        }

        BaseEntity that = (BaseEntity) o;

        return getUuid().equals(that.getUuid());
    }

    @Override
    public int hashCode() {
        return getUuid().hashCode();
    }
}
