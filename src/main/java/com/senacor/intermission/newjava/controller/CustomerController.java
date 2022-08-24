package com.senacor.intermission.newjava.controller;

import com.senacor.intermission.newjava.handler.CustomerHandler;
import com.senacor.intermission.newjava.model.api.ApiAccount;
import com.senacor.intermission.newjava.model.api.ApiCreateCustomer;
import com.senacor.intermission.newjava.model.api.ApiCustomer;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerHandler customerHandler;

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ApiCustomer> createCustomer(@Valid @RequestBody ApiCreateCustomer request) {
        return customerHandler.createCustomer(request);
    }

    @DeleteMapping(
        value = "/{customerUuid}",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Mono<Void> deleteCustomer(@PathVariable(name = "customerUuid") UUID customerUuid) {
        return customerHandler.deleteCustomer(customerUuid);
    }

    @GetMapping(
        value = "/{customerUuid}/accounts",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Flux<UUID> getAllAccounts(@PathVariable(value = "customerUuid") UUID customerUuid) {
        return customerHandler.getAllAccounts(customerUuid);
    }

    @PostMapping(
        value = "/{customerUuid}/accounts",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Mono<ApiAccount> createAccount(@PathVariable(value = "customerUuid") UUID customerUuid) {
        return customerHandler.createAccount(customerUuid);
    }
}
