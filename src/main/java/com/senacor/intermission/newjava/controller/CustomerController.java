package com.senacor.intermission.newjava.controller;

import com.senacor.intermission.newjava.handler.CustomerHandler;
import com.senacor.intermission.newjava.model.api.ApiAccount;
import com.senacor.intermission.newjava.model.api.ApiCreateCustomer;
import com.senacor.intermission.newjava.model.api.ApiCustomer;
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
public class CustomerController implements CustomerApi {

    private final CustomerHandler customerHandler;

    @Override
    public Mono<ResponseEntity<ApiCustomer>> createCustomer(
        Mono<ApiCreateCustomer> apiCreateCustomer,
        ServerWebExchange exchange
    ) {
        return apiCreateCustomer
            .flatMap(customerHandler::createCustomer)
            .map(ResponseEntity.status(HttpStatus.CREATED)::body);
    }

    @Override
    public Mono<ResponseEntity<Void>> deleteCustomer(
        UUID customerUuid,
        ServerWebExchange exchange
    ) {
        return customerHandler.deleteCustomer(customerUuid)
            .then(Mono.just(ResponseEntity.status(HttpStatus.NO_CONTENT).build()));
    }

    @Override
    public Mono<ResponseEntity<Flux<UUID>>> getAllAccounts(
        UUID customerUuid,
        ServerWebExchange exchange
    ) {
        Flux<UUID> result = customerHandler.getAllAccounts(customerUuid);
        return Mono.just(ResponseEntity.status(HttpStatus.OK).body(result));
    }

    @Override
    public Mono<ResponseEntity<ApiAccount>> createAccount(
        UUID customerUuid,
        ServerWebExchange exchange
    ) {
        return customerHandler.createAccount(customerUuid)
            .map(ResponseEntity.status(HttpStatus.CREATED)::body);
    }

}
