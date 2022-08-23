package com.senacor.intermission.newjava.handler;

import com.senacor.intermission.newjava.model.api.ApiAccount;
import com.senacor.intermission.newjava.model.api.ApiCreateCustomer;
import com.senacor.intermission.newjava.model.api.ApiCustomer;
import com.senacor.intermission.newjava.service.AccountService;
import java.math.BigInteger;
import java.util.Collection;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerHandler {

    private final AccountService accountService;

    public ApiCustomer createCustomer(ApiCreateCustomer request) {
        return null;
    }

    public void deleteCustomer(UUID customerUuid) {

    }

    public Collection<UUID> getAllAccounts(UUID customerUuid) {

    }

    public ApiAccount createAccount(UUID customerUuid) {
        BigInteger accountId = accountService.getNewAccountNumber();
    }

}
