package com.senacor.intermission.newjava.service;

import com.senacor.intermission.newjava.model.Account;
import com.senacor.intermission.newjava.model.Transaction;
import com.senacor.intermission.newjava.repository.TransactionRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;

    public Flux<Transaction> getTransactions(Account account) {
        return transactionRepository.findAllByAccountSortByDateDesc(account);
    }

    public Mono<Transaction> getTransaction(UUID transactionUuid) {
        return transactionRepository.findByUuid(transactionUuid)
            .switchIfEmpty(Mono.error(new IllegalArgumentException("Transaction not found!")));
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public Mono<Transaction> createTransaction(Transaction transaction) {
        if (!transaction.isNew()) {
            throw new IllegalStateException("Account is not new!");
        }
        return transactionRepository.save(transaction);
    }

}
