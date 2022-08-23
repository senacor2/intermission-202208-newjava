package com.senacor.intermission.newjava.handler;

import com.senacor.intermission.newjava.model.api.ApiAccount;
import com.senacor.intermission.newjava.model.api.ApiCreateTransaction;
import com.senacor.intermission.newjava.model.api.ApiTransaction;
import com.senacor.intermission.newjava.service.AccountService;
import com.senacor.intermission.newjava.service.IbanService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountHandler {

    private final AccountService accountService;
    private final IbanService ibanService;

    public ApiAccount createAccount(UUID customerUuid) {
        return null;
    }

    public ApiAccount getAccount(UUID accountUuid) {
        return null;
    }

    public List<ApiTransaction> getAllTransactions(UUID accountUuid) {
        return null;
    }

    public ApiTransaction createTransaction(UUID accountUuid, ApiCreateTransaction request) {
        return null;
    }

}
