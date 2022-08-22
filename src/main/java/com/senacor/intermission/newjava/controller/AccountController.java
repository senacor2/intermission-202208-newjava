package com.senacor.intermission.newjava.controller;

import com.senacor.intermission.newjava.model.Account;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@RestController()
public class AccountController {

    @PostMapping(value = "/customer/{customerUuid}/account", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Account> createAccount(@PathVariable(value = "customerUuid") UUID customerUuid) {
        // TODO Cetreate a new Account for the Customer and return its entity
        Account account = Account.builder().build();
        return new ResponseEntity<>(account, HttpStatus.CREATED);
    }

    @GetMapping(value = "/customer/{customerUuid}/account/{accountUuid}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Account> getAccount(@PathVariable(value = "customerUuid") UUID customerUuid,
                                              @PathVariable(value = "accountUuid") UUID accountUuid) {
        // TODO Find accounts by Customer UUID and Account Uuid
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/customer/{customerUuid}/account", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Account>> getAllAccounts(@PathVariable(value = "customerUuid") UUID customerUuid) {
        // TODO Find accounts by Customer UUID
        return new ResponseEntity<>(Collections.emptyList(), HttpStatus.OK);
    }
}
