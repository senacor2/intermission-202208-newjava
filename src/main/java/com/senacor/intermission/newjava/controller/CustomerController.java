package com.senacor.intermission.newjava.controller;

import com.senacor.intermission.newjava.model.Customer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController()
public class CustomerController {

    @PostMapping(value = "/customer/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Customer> createAccount(@RequestBody Customer customer) {
        // TODO Save and return customer
        return new ResponseEntity<>(customer, HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/customer/{customerUuid}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteAccount(@PathVariable(name = "customerUuid") UUID uuid) {
        // TODO Trigger asynchronous customer deletion
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
