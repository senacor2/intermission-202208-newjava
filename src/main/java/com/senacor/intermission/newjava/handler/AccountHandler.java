package com.senacor.intermission.newjava.handler;

import com.senacor.intermission.newjava.exceptions.IbanNotFoundException;
import com.senacor.intermission.newjava.mapper.ApiAccountMapper;
import com.senacor.intermission.newjava.mapper.ApiTransactionMapper;
import com.senacor.intermission.newjava.model.Account;
import com.senacor.intermission.newjava.model.Transaction;
import com.senacor.intermission.newjava.model.api.ApiAccount;
import com.senacor.intermission.newjava.model.api.ApiCreateTransaction;
import com.senacor.intermission.newjava.model.api.ApiTransaction;
import com.senacor.intermission.newjava.service.AccountService;
import com.senacor.intermission.newjava.service.IbanService;
import com.senacor.intermission.newjava.service.TransactionService;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
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
        Account senderAccount = accountService.getAccount(accountUuid);
        Account receiverAccount = accountService.getAccountByIban(request.getReceiverIban())
            .orElseThrow(() -> new IbanNotFoundException(request.getReceiverIban()));

        Transaction transaction = apiTransactionMapper.createTransaction(request, senderAccount, receiverAccount);
        transactionService.createTransaction(transaction);

        return apiTransactionMapper.toApiTransaction(transaction);
    }

}
