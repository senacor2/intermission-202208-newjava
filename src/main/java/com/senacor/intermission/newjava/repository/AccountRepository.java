package com.senacor.intermission.newjava.repository;

import com.senacor.intermission.newjava.model.Account;
import java.math.BigInteger;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

public interface AccountRepository extends JpaRepository<Account, BigInteger> {

    Optional<Account> findByIban(@NonNull String iban);

}
