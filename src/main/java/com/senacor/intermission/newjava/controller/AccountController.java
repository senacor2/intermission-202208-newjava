package com.senacor.intermission.newjava.controller;

import com.senacor.intermission.newjava.handler.AccountHandler;
import com.senacor.intermission.newjava.model.api.ApiAccount;
import com.senacor.intermission.newjava.model.api.ApiCreateTransaction;
import com.senacor.intermission.newjava.model.api.ApiTransaction;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
    public Mono<ResponseEntity<ApiAccount>> getAccount(@PathVariable(value = "uuid") UUID accountUuid) {
        return accountHandler.getAccount(accountUuid)
            .map(ResponseEntity.status(HttpStatus.OK)::body);
    }

    @GetMapping(
        value = "/{uuid}/transactions",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Mono<ResponseEntity<List<ApiTransaction>>> getAllTransactions(
        @PathVariable(value = "uuid") UUID accountUuid
    ) {
        return accountHandler.getAllTransactions(accountUuid)
            .collect(Collectors.toList())
            .map(ResponseEntity.status(HttpStatus.OK)::body);
    }

    @PostMapping(
        value = "/{uuid}/transactions",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Mono<ResponseEntity<ApiTransaction>> createTransaction(
        @PathVariable(value = "uuid") UUID accountUuid,
        @Valid @RequestBody ApiCreateTransaction request
    ) {
        return accountHandler.createTransaction(accountUuid, request)
            .map(ResponseEntity.status(HttpStatus.CREATED)::body);
    }

    @PostMapping(
        value = "/{uuid}/transactions/instant",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Mono<ResponseEntity<ApiTransaction>> createInstantTransaction(
        @PathVariable(value = "uuid") UUID accountUuid,
        @Valid @RequestBody ApiCreateTransaction request
    ) {
        return accountHandler.createInstantTransaction(accountUuid, request)
            .map(ResponseEntity.status(HttpStatus.CREATED)::body);
    }
}
