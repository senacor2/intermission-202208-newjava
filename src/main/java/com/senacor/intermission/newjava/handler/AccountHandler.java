package com.senacor.intermission.newjava.handler;

import com.senacor.intermission.newjava.exceptions.IbanNotFoundException;
import com.senacor.intermission.newjava.mapper.ApiAccountMapper;
import com.senacor.intermission.newjava.mapper.ApiTransactionMapper;
import com.senacor.intermission.newjava.model.Transaction;
import com.senacor.intermission.newjava.model.api.ApiAccount;
import com.senacor.intermission.newjava.model.api.ApiCreateTransaction;
import com.senacor.intermission.newjava.model.api.ApiTransaction;
import com.senacor.intermission.newjava.model.enums.TransactionStatus;
import com.senacor.intermission.newjava.service.AccountService;
import com.senacor.intermission.newjava.service.BalanceService;
import com.senacor.intermission.newjava.service.IbanService;
import com.senacor.intermission.newjava.service.TransactionService;
import java.time.Duration;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class AccountHandler {

    private final AccountService accountService;
    private final ApiAccountMapper apiAccountMapper;
    private final ApiTransactionMapper apiTransactionMapper;
    private final TransactionService transactionService;
    private final IbanService ibanService;
    private final TransactionalOperator transactionalOperator;
    private final BalanceService balanceService;

    @Transactional(readOnly = true)
    public Mono<ApiAccount> getAccount(UUID accountUuid) {
        return accountService.getAccount(accountUuid)
            .zipWhen(balanceService::getBalanceForAccount, apiAccountMapper::toApiAccount);
    }

    @Transactional(readOnly = true)
    public Flux<ApiTransaction> getAllTransactions(UUID accountUuid) {
        return accountService.getAccount(accountUuid)
            .flatMapMany(transactionService::getTransactions)
            .flatMap(this::toApiTransaction);
    }

    @Transactional
    public Mono<ApiTransaction> createTransaction(UUID accountUuid, ApiCreateTransaction request) {
        return createTransactionInternal(accountUuid, request)
            .flatMap(this::toApiTransaction);
    }

    public Mono<ApiTransaction> createInstantTransaction(UUID accountUuid, ApiCreateTransaction request) {
        return createTransactionInternal(accountUuid, request)
            .transform(transactionalOperator::transactional)
            .onErrorStop()
            .map(Transaction::getUuid)
            .flatMap(it -> transactionalOperator.transactional(awaitCompletedTransaction(it)))
            .flatMap(this::toApiTransaction);
    }

    private Mono<Transaction> awaitCompletedTransaction(UUID transactionUuid) {
        return transactionService.getTransaction(transactionUuid)
            .filter(it -> it.getStatus() != TransactionStatus.PENDING)
            .repeatWhenEmpty(100, in -> in.delayElements(Duration.ofMillis(200)));
    }

    private Mono<Transaction> createTransactionInternal(UUID accountUuid, ApiCreateTransaction request) {
        return accountService.getAccountByIban(request.receiverIban())
            .switchIfEmpty(Mono.error(new IbanNotFoundException(request.receiverIban())))
            .zipWith(
                accountService.getAccount(accountUuid),
                (receiver, sender) -> apiTransactionMapper.createTransaction(request, sender, receiver)
            ).flatMap(transactionService::createTransaction);
    }

    private Mono<ApiTransaction> toApiTransaction(Transaction transaction) {
        return accountService.getAccount(transaction.getSenderId())
            .zipWith(accountService.getAccount(transaction.getReceiverId()), (sender, receiver) ->
                apiTransactionMapper.toApiTransaction(transaction, sender, receiver));
    }
}
