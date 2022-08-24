package com.senacor.intermission.newjava.controller;

import com.senacor.intermission.newjava.handler.AccountHandler;
import com.senacor.intermission.newjava.model.api.ApiAccount;
import com.senacor.intermission.newjava.model.api.ApiCreateTransaction;
import com.senacor.intermission.newjava.model.api.ApiTransaction;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountHandler accountHandler;

    @GetMapping(
        value = "/{uuid}",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Valid
    public ResponseEntity<ApiAccount> getAccount(@PathVariable(value = "uuid") UUID accountUuid) {
        ApiAccount result = accountHandler.getAccount(accountUuid);
        return ResponseEntity.ok(result);
    }

    @GetMapping(
        value = "/{uuid}/transactions",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Valid
    public ResponseEntity<List<ApiTransaction>> getAllTransactions(
        @PathVariable(value = "uuid") UUID accountUuid
    ) {
        List<ApiTransaction> result = accountHandler.getAllTransactions(accountUuid);
        return ResponseEntity.ok(result);
    }

    @PostMapping(
        value = "/{uuid}/transactions",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Valid
    public ResponseEntity<ApiTransaction> createTransaction(
        @PathVariable(value = "uuid") UUID accountUuid,
        @Valid @RequestBody ApiCreateTransaction request
    ) {
        ApiTransaction result = accountHandler.createTransaction(accountUuid, request);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @PostMapping(
        value = "/{uuid}/transactions/instant",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Valid
    public ResponseEntity<ApiTransaction> createInstantTransaction(
        @PathVariable(value = "uuid") UUID accountUuid,
        @Valid @RequestBody ApiCreateTransaction request
    ) {
        ApiTransaction result = accountHandler.createInstantTransaction(accountUuid, request);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }
}
