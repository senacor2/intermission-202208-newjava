package com.senacor.intermission.newjava.handler;

import com.senacor.intermission.newjava.exceptions.IbanNotFoundException;
import com.senacor.intermission.newjava.mapper.ApiAccountMapper;
import com.senacor.intermission.newjava.mapper.ApiTransactionMapper;
import com.senacor.intermission.newjava.model.Account;
import com.senacor.intermission.newjava.model.Transaction;
import com.senacor.intermission.newjava.model.api.ApiAccount;
import com.senacor.intermission.newjava.model.api.ApiCreateTransaction;
import com.senacor.intermission.newjava.model.api.ApiTransaction;
import com.senacor.intermission.newjava.model.enums.TransactionStatus;
import com.senacor.intermission.newjava.service.*;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import javax.swing.*;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AccountHandler {

    private final AccountService accountService;
    private final ApiAccountMapper apiAccountMapper;
    private final ApiTransactionMapper apiTransactionMapper;
    private final TransactionService transactionService;
    private final IbanService ibanService;
    private final DbTransactionalService dbTransactionalService;
    private final PushNotificationService pushNotificationService;

    @Transactional(readOnly = true)
    public ApiAccount getAccount(UUID accountUuid) {
        Account account = accountService.getAccount(accountUuid);
        return apiAccountMapper.toApiAccount(account);
    }

    @Transactional(readOnly = true)
    public List<ApiTransaction> getAllTransactions(UUID accountUuid) {
        Account account = accountService.getAccount(accountUuid);
        return transactionService.getTransactions(account)
            .stream()
            .map(apiTransactionMapper::toApiTransaction)
            .collect(Collectors.toList());
    }

    @Transactional
    public ApiTransaction createTransaction(UUID accountUuid, ApiCreateTransaction request) {
        Transaction transaction = createTransactionInternal(accountUuid, request);
        return apiTransactionMapper.toApiTransaction(transaction);
    }

    public ApiTransaction createInstantTransaction(UUID accountUuid, ApiCreateTransaction request) {
        Transaction transaction = dbTransactionalService.doTransactional(() -> createTransactionInternal(accountUuid, request));
        UUID uuid = transaction.getUuid();
        Timer timer = new Timer(200, null);
        CompletableFuture<Transaction> future = new CompletableFuture<>();
        timer.addActionListener(ignored -> dbTransactionalService.doTransactional(() -> {
            Transaction tx = transactionService.getTransaction(uuid);
            if (tx.getStatus() != TransactionStatus.PENDING) {
                future.complete(tx);
                timer.stop();
            }
        }));
        timer.start();
        return future.thenApply(apiTransactionMapper::toApiTransaction).join();
    }

    private Transaction createTransactionInternal(UUID accountUuid, ApiCreateTransaction request) {
        Account senderAccount = accountService.getAccount(accountUuid);
        Account receiverAccount = accountService.getAccountByIban(request.receiverIban())
            .orElseThrow(() -> new IbanNotFoundException(request.receiverIban()));

        Transaction transaction = apiTransactionMapper.createTransaction(request, senderAccount, receiverAccount);
        transaction = transactionService.createTransaction(transaction);
        // This call is executed asynchronously
        pushNotificationService.sendPushNotification(transaction.getUuid());
        return transaction;
    }
}
