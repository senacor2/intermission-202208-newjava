package com.senacor.intermission.newjava.controller;

import com.senacor.intermission.newjava.handler.CustomerHandler;
import com.senacor.intermission.newjava.model.api.ApiAccount;
import com.senacor.intermission.newjava.model.api.ApiCreateCustomer;
import com.senacor.intermission.newjava.model.api.ApiCustomer;
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
@RequestMapping("/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerHandler customerHandler;

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<ApiCustomer>> createCustomer(@Valid @RequestBody ApiCreateCustomer request) {
        return customerHandler.createCustomer(request)
            .map(ResponseEntity.status(HttpStatus.CREATED)::body);
    }

    @DeleteMapping(
        value = "/{customerUuid}",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Mono<ResponseEntity<Void>> deleteCustomer(@PathVariable(name = "customerUuid") UUID customerUuid) {
        return customerHandler.deleteCustomer(customerUuid)
            .then(Mono.just(ResponseEntity.status(HttpStatus.NO_CONTENT).build()));
    }

    @GetMapping(
        value = "/{customerUuid}/accounts",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Mono<ResponseEntity<List<UUID>>> getAllAccounts(@PathVariable(value = "customerUuid") UUID customerUuid) {
        return customerHandler.getAllAccounts(customerUuid)
            .collect(Collectors.toList())
            .map(ResponseEntity.status(HttpStatus.OK)::body);
    }

    @PostMapping(
        value = "/{customerUuid}/accounts",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Mono<ResponseEntity<ApiAccount>> createAccount(@PathVariable(value = "customerUuid") UUID customerUuid) {
        return customerHandler.createAccount(customerUuid)
            .map(ResponseEntity.status(HttpStatus.CREATED)::body);
    }
}
