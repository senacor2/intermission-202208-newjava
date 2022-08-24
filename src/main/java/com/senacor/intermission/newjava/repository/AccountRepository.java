package com.senacor.intermission.newjava.repository;

import com.senacor.intermission.newjava.model.Account;
import java.math.BigInteger;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;

public interface AccountRepository extends JpaRepository<Account, BigInteger> {

    @Query(value = "SELECT NEXT VALUE FOR ACCOUNT_NUMBER FROM dual;", nativeQuery = true)
    BigInteger getNextAccountNumber();

    Optional<Account> findByUuid(@NonNull UUID uuid);
    Optional<Account> findByIban(@NonNull String iban);

}
