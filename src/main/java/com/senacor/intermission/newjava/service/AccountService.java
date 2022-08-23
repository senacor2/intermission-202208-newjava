package com.senacor.intermission.newjava.service;

import com.senacor.intermission.newjava.exceptions.AccountNotFoundException;
import com.senacor.intermission.newjava.model.Account;
import com.senacor.intermission.newjava.repository.AccountRepository;
import java.math.BigInteger;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    @Transactional(propagation = Propagation.MANDATORY)
    public Account createAccount(Account account) {
        if (!account.isNew()) {
            throw new IllegalStateException("Account is not new!");
        }
        return accountRepository.save(account);
    }

    public BigInteger getNewAccountNumber() {
        return accountRepository.getNextAccountNumber();
    }

    public Account getAccount(UUID accountUuid) {
        return accountRepository.findByUuid(accountUuid)
            .orElseThrow(() -> new AccountNotFoundException(accountUuid));
    }

    public Optional<Account> getAccountByIban(String iban) {
        return accountRepository.findByIban(iban);
    }

}
