package com.senacor.intermission.newjava.model;

import java.time.LocalDate;
import lombok.*;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Table(name = "CUSTOMER")
public class Customer extends BaseEntity {

    private String prename;
    private String lastname;
    private LocalDate dateOfBirth;

}
