package com.senacor.intermission.newjava.repository;

import com.senacor.intermission.newjava.model.Account;
import com.senacor.intermission.newjava.model.Transaction;
import com.senacor.intermission.newjava.model.enums.TransactionStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, BigInteger> {

    Optional<Transaction> findByUuid(UUID uuid);

    @Query("""
        SELECT t
          FROM Transaction t
         WHERE t.sender=:account
            OR t.receiver=:account
         ORDER BY t.transactionTime DESC""")
    List<Transaction> findAllByAccountSortByDateDesc(@Param("account") Account account);

    @Query("""
        SELECT t.id
          FROM Transaction t
         WHERE t.status=:status
           and (t.transactionTime<:time or t.transactionTime is null)""")
    Page<BigInteger> findAllByStatusAndTimeBefore(
        @Param("status") TransactionStatus status,
        @Param("time") LocalDateTime transactionTimeBefore,
        Pageable pageable
    );

}
