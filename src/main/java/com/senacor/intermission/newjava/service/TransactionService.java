package com.senacor.intermission.newjava.service;

import com.senacor.intermission.newjava.model.Account;
import com.senacor.intermission.newjava.model.Transaction;
import com.senacor.intermission.newjava.repository.TransactionRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;

    public List<Transaction> getTransactions(Account account) {
        return transactionRepository.findAllByAccountSortByDateDesc(account);
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public Transaction createTransaction(Transaction transaction) {
        if (!transaction.isNew()) {
            throw new IllegalStateException("Account is not new!");
        }
        return transactionRepository.save(transaction);
    }

}
