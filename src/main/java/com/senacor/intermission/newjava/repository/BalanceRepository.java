package com.senacor.intermission.newjava.repository;

import com.senacor.intermission.newjava.model.Account;
import com.senacor.intermission.newjava.model.Balance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigInteger;

public interface BalanceRepository extends JpaRepository<Balance, BigInteger> {

    Balance getByAccountIban(String iban);

    Balance getByAccount(Account account);

    @Modifying
    @Query("""
        UPDATE Balance b
           SET b.valueInCents=b.valueInCents+:amount
         WHERE b.account=:account""")
    int updateByAccountIbanIncrementBalance(
        @Param("account") Account account,
        @Param("amount") BigInteger incrementAmount
    );

}
