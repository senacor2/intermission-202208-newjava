package com.senacor.intermission.newjava.service;

import com.senacor.intermission.newjava.model.Account;
import com.senacor.intermission.newjava.model.Transaction;
import com.senacor.intermission.newjava.repository.TransactionRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;

    public List<Transaction> getTransactions(Account account) {
        return transactionRepository.findAllByAccountSortByDateDesc(account);
    }

    public Transaction getTransaction(UUID transactionUuid) {
        return transactionRepository.findByUuid(transactionUuid)
            .orElseThrow(() -> new IllegalArgumentException("Transaction not found!"));
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public Transaction createTransaction(Transaction transaction) {
        if (!transaction.isNew()) {
            throw new IllegalStateException("Account is not new!");
        }
        return transactionRepository.save(transaction);
    }

}
