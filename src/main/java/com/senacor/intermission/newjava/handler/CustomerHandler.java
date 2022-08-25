package com.senacor.intermission.newjava.handler;

import com.senacor.intermission.newjava.exceptions.NoFurtherAccountAllowedException;
import com.senacor.intermission.newjava.mapper.ApiAccountMapper;
import com.senacor.intermission.newjava.mapper.ApiCustomerMapper;
import com.senacor.intermission.newjava.model.*;
import com.senacor.intermission.newjava.model.api.ApiAccount;
import com.senacor.intermission.newjava.model.api.ApiCreateCustomer;
import com.senacor.intermission.newjava.model.api.ApiCustomer;
import com.senacor.intermission.newjava.service.AccountService;
import com.senacor.intermission.newjava.service.CustomerService;
import com.senacor.intermission.newjava.service.IbanService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.Collection;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerHandler {

    private final ApiCustomerMapper apiCustomerMapper;
    private final ApiAccountMapper apiAccountMapper;
    private final CustomerService customerService;
    private final AccountService accountService;
    private final IbanService ibanService;

    @Transactional
    public ApiCustomer createCustomer(ApiCreateCustomer request) {
        // Switch Expression
        Customer customer = switch(request.type()) {
            case BASE: yield apiCustomerMapper.toOwnBaseCustomer(request);
            case PREMIUM: yield apiCustomerMapper.toOwnPremiumCustomer(request);
        };
        customerService.createCustomer(customer);
        return apiCustomerMapper.toApiCustomer(customer, request.type());
    }

    @Transactional
    public void deleteCustomer(UUID customerUuid) {
        Customer customer = customerService.findCustomer(customerUuid);
        customerService.deleteCustomer(customer);
    }

    @Transactional(readOnly = true)
    public Collection<UUID> getAllAccounts(UUID customerUuid) {
        Customer customer = customerService.findCustomer(customerUuid);
        return customer.getAccounts().stream()
            .map(Account::getUuid)
            .collect(Collectors.toSet());
    }

    @Transactional
    public ApiAccount createAccount(UUID customerUuid) {
        Customer customer = customerService.findCustomer(customerUuid);
        if (!isAdditionalAccountAllowed(customer)) {
            throw new NoFurtherAccountAllowedException(customerUuid);
        }

        BigInteger accountNumber = accountService.getNewAccountNumber();
        String iban = ibanService.generateIban(accountNumber);
        Balance balance = Balance.builder().valueInCents(BigInteger.ZERO).build();
        Account account = Account.builder()
            .accountNumber(accountNumber)
            .customer(customer)
            .iban(iban)
            .balance(balance)
            .build();
        balance.setAccount(account);
        Account result = accountService.createAccount(account);
        return apiAccountMapper.toApiAccount(result);
    }

    private boolean isAdditionalAccountAllowed(Customer customer) {
        // Preview-Feature (Pattern Matching for switch)
        return switch (customer) {
            case PremiumCustomer premiumCustomer -> true;
            case BaseCustomer baseCustomer -> customer.getAccounts().size() < baseCustomer.maxNumberOfAccounts();
            // Customer cannot be a sealed class (JPA does not like it). Thus, we need a Customer case here.
            case Customer other -> false;
        };
    }
}
