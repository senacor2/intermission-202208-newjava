package com.senacor.intermission.newjava.handler;

import com.senacor.intermission.newjava.mapper.ApiAccountMapper;
import com.senacor.intermission.newjava.mapper.ApiCustomerMapper;
import com.senacor.intermission.newjava.model.Account;
import com.senacor.intermission.newjava.model.Balance;
import com.senacor.intermission.newjava.model.Customer;
import com.senacor.intermission.newjava.model.api.ApiAccount;
import com.senacor.intermission.newjava.model.api.ApiCreateCustomer;
import com.senacor.intermission.newjava.model.api.ApiCustomer;
import com.senacor.intermission.newjava.service.AccountService;
import com.senacor.intermission.newjava.service.CustomerService;
import com.senacor.intermission.newjava.service.IbanService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerHandler {

    private final ApiCustomerMapper apiCustomerMapper;
    private final CustomerService customerService;
    private final ApiAccountMapper apiAccountMapper;
    private final AccountService accountService;
    private final IbanService ibanService;

    public ApiCustomer createCustomer(ApiCreateCustomer request) {
        Customer customer = apiCustomerMapper.toOwnCustomer(request);
        customerService.createCustomer(customer);
        return apiCustomerMapper.toApiCustomer(customer);
    }

    public void deleteCustomer(UUID customerUuid) {
        Customer customer = customerService.findCustomer(customerUuid);
        customerService.deleteCustomer(customer);
    }

    public Collection<UUID> getAllAccounts(UUID customerUuid) {
        Customer customer = customerService.findCustomer(customerUuid);
        return customer.getAccounts().stream()
            .map(Account::getUuid)
            .collect(Collectors.toSet());
    }

    public ApiAccount createAccount(UUID customerUuid) {
        Customer customer = customerService.findCustomer(customerUuid);
        BigInteger accountId = accountService.getNewAccountNumber();
        String iban = ibanService.generateIban(accountId);
        Account account = Account.builder()
            .customer(customer)
            .iban(iban)
            .build();
        Balance balance = Balance.builder()
            .account(account)
            .valueInCents(BigInteger.ZERO)
            .lastUpdate(LocalDateTime.now())
            .build();
        account.setBalance(balance);
        Account result = accountService.createAccount(account);
        // TODO Do we have to add the account to the customer and to save the customer?
        return apiAccountMapper.toApiAccount(result);
    }
}
