package com.senacor.intermission.newjava.repository;

import com.senacor.intermission.newjava.model.Transaction;
import com.senacor.intermission.newjava.model.enums.TransactionStatus;
import java.math.BigInteger;
import java.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TransactionRepository extends JpaRepository<Transaction, BigInteger> {

    @Query("SELECT t.id FROM Transaction t WHERE t.status=:status and (t.transactionTime<:time or t.transactionTime is null)")
    Page<BigInteger> findAllByStatusAndTimeBefore(
        @Param("status") TransactionStatus status,
        @Param("time") LocalDateTime transactionTimeBefore,
        Pageable pageable
    );

}
