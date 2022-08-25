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
import java.math.BigInteger;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        Customer customer = apiCustomerMapper.toOwnCustomer(request);
        customerService.createCustomer(customer);
        return apiCustomerMapper.toApiCustomer(customer);
    }

    @Transactional
    public void deleteCustomer(UUID customerUuid) {
        Customer customer = customerService.findCustomer(customerUuid);
        customerService.deleteCustomer(customer);
    }

    @Transactional(readOnly = true)
    public List<UUID> getAllAccounts(UUID customerUuid) {
        Customer customer = customerService.findCustomer(customerUuid);
        return customer.getAccounts().stream()
            .map(Account::getUuid)
            .collect(Collectors.toList());
    }

    @Transactional
    public ApiAccount createAccount(UUID customerUuid) {
        Customer customer = customerService.findCustomer(customerUuid);
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
}
