package com.senacor.intermission.newjava.repository;

import com.senacor.intermission.newjava.model.Account;
import com.senacor.intermission.newjava.model.Transaction;
import com.senacor.intermission.newjava.model.enums.TransactionStatus;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TransactionRepository extends R2dbcRepository<Transaction, BigInteger> {

    Mono<Transaction> findByUuid(UUID uuid);

    @Query("""
        SELECT t.*
          FROM Transaction t
         WHERE t.SENDER_ID=:account
            OR t.RECEIVER_ID=:account
         ORDER BY t.TRANSACTION_TIME DESC""")
    Flux<Transaction> findAllByAccountSortByDateDesc(@Param("account") Account account);

    @Query("""
        SELECT t.id
          FROM Transaction t
         WHERE t.STATUS=:status
           and (t.TRANSACTION_TIME<:time or t.TRANSACTION_TIME is null)""")
    Flux<BigInteger> findAllByStatusAndTimeBefore(
        @Param("status") TransactionStatus status,
        @Param("time") LocalDateTime transactionTimeBefore,
        Pageable pageable
    );

}
