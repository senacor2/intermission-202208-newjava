package com.senacor.intermission.newjava.controller;

import com.senacor.intermission.newjava.handler.AccountHandler;
import com.senacor.intermission.newjava.model.api.ApiAccount;
import com.senacor.intermission.newjava.model.api.ApiCreateTransaction;
import com.senacor.intermission.newjava.model.api.ApiTransaction;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class AccountController implements AccountApi {

    private final AccountHandler accountHandler;

    @Override
    public Mono<ResponseEntity<ApiAccount>> getAccount(UUID accountUuid, ServerWebExchange exchange) {
        return accountHandler.getAccount(accountUuid)
            .map(ResponseEntity.status(HttpStatus.OK)::body);
    }

    @Override
    public Mono<ResponseEntity<Flux<ApiTransaction>>> getAllTransactions(UUID accountUuid, ServerWebExchange exchange) {
        Flux<ApiTransaction> body = accountHandler.getAllTransactions(accountUuid);
        return Mono.just(ResponseEntity.status(HttpStatus.OK).body(body));
    }

    @Override
    public Mono<ResponseEntity<ApiTransaction>> createTransaction(
        UUID accountUuid,
        Mono<ApiCreateTransaction> apiCreateTransaction,
        ServerWebExchange exchange
    ) {
        return apiCreateTransaction
            .flatMap(it -> accountHandler.createTransaction(accountUuid, it))
            .map(ResponseEntity.status(HttpStatus.CREATED)::body);
    }

    @Override
    public Mono<ResponseEntity<ApiTransaction>> createInstantTransaction(
        UUID accountUuid,
        Mono<ApiCreateTransaction> apiCreateTransaction,
        ServerWebExchange exchange
    ) {
        return apiCreateTransaction
            .flatMap(it -> accountHandler.createInstantTransaction(accountUuid, it))
            .map(ResponseEntity.status(HttpStatus.CREATED)::body);
    }

}
