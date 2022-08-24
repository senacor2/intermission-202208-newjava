package com.senacor.intermission.newjava.controller;

import com.senacor.intermission.newjava.handler.CustomerHandler;
import com.senacor.intermission.newjava.model.api.ApiAccount;
import com.senacor.intermission.newjava.model.api.ApiCreateCustomer;
import com.senacor.intermission.newjava.model.api.ApiCustomer;
import jakarta.validation.Valid;
import java.util.Collection;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerHandler customerHandler;

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Valid
    public ResponseEntity<ApiCustomer> createCustomer(@RequestBody ApiCreateCustomer request) {
        ApiCustomer result = customerHandler.createCustomer(request);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/{customerUuid}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Valid
    public ResponseEntity<Void> deleteCustomer(@PathVariable(name = "customerUuid") UUID customerUuid) {
        customerHandler.deleteCustomer(customerUuid);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/{customerUuid}/accounts", produces = MediaType.APPLICATION_JSON_VALUE)
    @Valid
    public ResponseEntity<Collection<UUID>> getAllAccounts(@PathVariable(value = "customerUuid") UUID customerUuid) {
        Collection<UUID> result = customerHandler.getAllAccounts(customerUuid);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping(value = "/{customerUuid}/accounts", produces = MediaType.APPLICATION_JSON_VALUE)
    @Valid
    public ResponseEntity<ApiAccount> createAccount(@PathVariable(value = "customerUuid") UUID customerUuid) {
        ApiAccount result = customerHandler.createAccount(customerUuid);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }
}
