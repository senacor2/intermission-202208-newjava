package com.senacor.intermission.newjava.controller;

import com.senacor.intermission.newjava.mapper.ApiCustomerMapper;
import com.senacor.intermission.newjava.model.Customer;
import com.senacor.intermission.newjava.model.api.ApiCustomer;
import com.senacor.intermission.newjava.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController()
public class CustomerController {

    @Autowired
    private CustomerService customerService;
    @Autowired
    private ApiCustomerMapper apiCustomerMapper;

    @PostMapping(value = "/customer/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiCustomer> createCustomer(@RequestBody ApiCustomer customer) {
        Customer result = customerService.createCustomer(apiCustomerMapper.toOwnCustomer(customer));
        return new ResponseEntity<>(apiCustomerMapper.toApiCustomer(result), HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/customer/{customerUuid}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteCustomer(@PathVariable(name = "customerUuid") UUID customerUuid) {
        boolean success = customerService.deleteCustomer(customerUuid);
        return new ResponseEntity<>(success ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }
}
