package com.senacor.intermission.newjava.controller;

import com.senacor.intermission.newjava.handler.AccountHandler;
import com.senacor.intermission.newjava.model.api.ApiAccount;
import com.senacor.intermission.newjava.model.api.ApiCreateTransaction;
import com.senacor.intermission.newjava.model.api.ApiTransaction;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountHandler accountHandler;

    @GetMapping(
        value = "/{uuid}",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Mono<ApiAccount> getAccount(@PathVariable(value = "uuid") UUID accountUuid) {
        return accountHandler.getAccount(accountUuid);
    }

    @GetMapping(
        value = "/{uuid}/transactions",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Flux<ApiTransaction> getAllTransactions(
        @PathVariable(value = "uuid") UUID accountUuid
    ) {
        return accountHandler.getAllTransactions(accountUuid);
    }

    @PostMapping(
        value = "/{uuid}/transactions",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Mono<ApiTransaction> createTransaction(
        @PathVariable(value = "uuid") UUID accountUuid,
        @Valid @RequestBody ApiCreateTransaction request
    ) {
        return accountHandler.createTransaction(accountUuid, request);
    }

    @PostMapping(
        value = "/{uuid}/transactions/instant",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Mono<ApiTransaction> createInstantTransaction(
        @PathVariable(value = "uuid") UUID accountUuid,
        @Valid @RequestBody ApiCreateTransaction request
    ) {
        return accountHandler.createInstantTransaction(accountUuid, request);
    }
}
