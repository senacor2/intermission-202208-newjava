package com.senacor.intermission.newjava.repository;

import com.senacor.intermission.newjava.model.Customer;
import java.math.BigInteger;
import java.util.UUID;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

public interface CustomerRepository extends R2dbcRepository<Customer, BigInteger> {

    Mono<Customer> findByUuid(UUID uuid);
}
