package com.senacor.intermission.newjava.controller;

import com.senacor.intermission.newjava.handler.AccountHandler;
import com.senacor.intermission.newjava.model.api.ApiAccount;
import com.senacor.intermission.newjava.model.api.ApiCreateTransaction;
import com.senacor.intermission.newjava.model.api.ApiTransaction;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AccountController implements AccountApi {

    private final AccountHandler accountHandler;

    @Override
    public ResponseEntity<ApiAccount> getAccount(UUID accountUuid) {
        ApiAccount result = accountHandler.getAccount(accountUuid);
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<List<ApiTransaction>> getAllTransactions(
        UUID accountUuid
    ) {
        List<ApiTransaction> result = accountHandler.getAllTransactions(accountUuid);
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<ApiTransaction> createTransaction(
        UUID accountUuid,
        ApiCreateTransaction request
    ) {
        ApiTransaction result = accountHandler.createTransaction(accountUuid, request);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<ApiTransaction> createInstantTransaction(
        UUID accountUuid,
        ApiCreateTransaction request
    ) {
        ApiTransaction result = accountHandler.createInstantTransaction(accountUuid, request);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }
}
