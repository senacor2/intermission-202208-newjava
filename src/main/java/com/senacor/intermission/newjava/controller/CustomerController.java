package com.senacor.intermission.newjava.controller;

import com.senacor.intermission.newjava.handler.CustomerHandler;
import com.senacor.intermission.newjava.model.api.ApiAccount;
import com.senacor.intermission.newjava.model.api.ApiCreateCustomer;
import com.senacor.intermission.newjava.model.api.ApiCustomer;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CustomerController implements CustomerApi {

    private final CustomerHandler customerHandler;

    @Override
    public ResponseEntity<ApiCustomer> createCustomer(ApiCreateCustomer request) {
        ApiCustomer result = customerHandler.createCustomer(request);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Void> deleteCustomer(UUID customerUuid) {
        customerHandler.deleteCustomer(customerUuid);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<List<UUID>> getAllAccounts(UUID customerUuid) {
        List<UUID> result = customerHandler.getAllAccounts(customerUuid);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ApiAccount> createAccount(UUID customerUuid) {
        ApiAccount result = customerHandler.createAccount(customerUuid);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }
}
