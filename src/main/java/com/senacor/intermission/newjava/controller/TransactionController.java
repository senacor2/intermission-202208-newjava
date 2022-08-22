package com.senacor.intermission.newjava.controller;

import com.senacor.intermission.newjava.model.Transaction;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@RestController()
public class TransactionController {

    @PostMapping(value = "/customer/{customerUuid}/account/{accountUuid}/transaction", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Transaction> createTransaction(@PathVariable(name = "customerUuid") UUID customerUuid,
                                                         @PathVariable(name = "accountUuid") UUID accountUuid,
                                                         @RequestBody Transaction transaction) {
        // TODO validate and create transaction
        return new ResponseEntity<>(transaction, HttpStatus.CREATED);
    }

    @GetMapping(value = "/customer/{customerUuid}/account/{accountUuid}/transaction", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Transaction>> getAllTransactions(@PathVariable(value = "customerUuid") UUID customerUuid,
                                                                @PathVariable(name = "accountUuid") UUID accountUuid) {
        // TODO Find transactions by Customer UUID and Account UUID
        return new ResponseEntity<>(Collections.emptyList(), HttpStatus.OK);
    }
}
