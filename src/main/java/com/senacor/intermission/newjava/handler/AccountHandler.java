package com.senacor.intermission.newjava.handler;

import com.senacor.intermission.newjava.model.Transaction;
import com.senacor.intermission.newjava.model.api.ApiAccount;
import com.senacor.intermission.newjava.model.api.ApiCreateTransaction;
import com.senacor.intermission.newjava.model.api.ApiTransaction;
import com.senacor.intermission.newjava.service.AccountService;
import com.senacor.intermission.newjava.service.IbanService;
import java.math.BigInteger;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountHandler {

    private final AccountService accountService;
    private final IbanService ibanService;

    public ApiAccount createAccount(UUID customerUuid) {


    }

    public ApiAccount getAccount(UUID accountUuid) {

    }

    public List<ApiTransaction> getAllTransactions(UUID accountUuid) {

    }

    public ApiTransaction createTransaction(UUID accountUuid, ApiCreateTransaction request) {

    }

}
