package com.senacor.intermission.newjava.repository;

import com.senacor.intermission.newjava.model.Account;
import com.senacor.intermission.newjava.model.Balance;
import java.math.BigInteger;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import reactor.core.publisher.Mono;

public interface BalanceRepository extends R2dbcRepository<Balance, BigInteger> {

    @Query("SELECT b.* FROM BALANCE b JOIN ACCOUNT A on A.ID = b.ACCOUNT_ID WHERE a.IBAN=:iban")
    Mono<Balance> getByAccountIban(@Param("iban") String iban);

    Mono<Balance> getByAccountId(BigInteger accountId);

    @Modifying
    @Query("""
        UPDATE BALANCE b
           SET b.VALUE_IN_CENTS=b.VALUE_IN_CENTS+:amount
         WHERE b.ACCOUNT_ID=:account""")
    Mono<Integer> updateByAccountIbanIncrementBalance(
        @Param("account") Account account,
        @Param("amount") BigInteger incrementAmount
    );

}
