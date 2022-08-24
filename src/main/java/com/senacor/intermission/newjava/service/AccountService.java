package com.senacor.intermission.newjava.service;

import com.senacor.intermission.newjava.exceptions.AccountNotFoundException;
import com.senacor.intermission.newjava.model.Account;
import com.senacor.intermission.newjava.model.Customer;
import com.senacor.intermission.newjava.repository.AccountRepository;
import java.math.BigInteger;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    @Transactional(propagation = Propagation.MANDATORY)
    public Mono<Account> createAccount(Account account) {
        if (!account.isNew()) {
            throw new IllegalStateException("Account is not new!");
        }
        return accountRepository.save(account);
    }

    public Mono<BigInteger> getNewAccountNumber() {
        return accountRepository.getNextAccountNumber();
    }

    public Mono<Account> getAccount(BigInteger accountId) {
        return accountRepository.findById(accountId)
            .switchIfEmpty(Mono.error(new AccountNotFoundException(accountId)));
    }

    public Mono<Account> getAccount(UUID accountUuid) {
        return accountRepository.findByUuid(accountUuid)
            .switchIfEmpty(Mono.error(new AccountNotFoundException(accountUuid)));
    }

    public Mono<Account> getAccountByIban(String iban) {
        return accountRepository.findByIban(iban);
    }

    public Flux<Account> findByCustomer(Customer customer) {
        return accountRepository.findAllByCustomerId(customer.getId());
    }

}
