package com.senacor.intermission.newjava.repository;

import com.senacor.intermission.newjava.model.Account;
import java.math.BigInteger;
import java.util.UUID;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.lang.NonNull;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AccountRepository extends R2dbcRepository<Account, BigInteger> {

    @Query(value = """
        SELECT NEXT VALUE FOR ACCOUNT_NUMBER
        FROM dual""")
    Mono<BigInteger> getNextAccountNumber();

    Mono<Account> findByUuid(@NonNull UUID uuid);

    Mono<Account> findByIban(@NonNull String iban);

    Flux<Account> findAllByCustomerId(BigInteger customerId);

}
