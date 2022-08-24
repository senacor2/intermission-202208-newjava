package com.senacor.intermission.newjava.handler;

import com.senacor.intermission.newjava.mapper.ApiAccountMapper;
import com.senacor.intermission.newjava.mapper.ApiCustomerMapper;
import com.senacor.intermission.newjava.model.Account;
import com.senacor.intermission.newjava.model.Customer;
import com.senacor.intermission.newjava.model.api.ApiAccount;
import com.senacor.intermission.newjava.model.api.ApiCreateCustomer;
import com.senacor.intermission.newjava.model.api.ApiCustomer;
import com.senacor.intermission.newjava.service.AccountService;
import com.senacor.intermission.newjava.service.BalanceService;
import com.senacor.intermission.newjava.service.CustomerService;
import com.senacor.intermission.newjava.service.IbanService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CustomerHandler {

    private final ApiCustomerMapper apiCustomerMapper;
    private final ApiAccountMapper apiAccountMapper;
    private final CustomerService customerService;
    private final AccountService accountService;
    private final IbanService ibanService;
    private final BalanceService balanceService;

    @Transactional
    public Mono<ApiCustomer> createCustomer(ApiCreateCustomer request) {
        Customer customer = apiCustomerMapper.toOwnCustomer(request);
        return customerService.createCustomer(customer)
            .map(apiCustomerMapper::toApiCustomer);
    }

    @Transactional
    public Mono<Void> deleteCustomer(UUID customerUuid) {
        return customerService.findCustomer(customerUuid)
            .flatMap(customerService::deleteCustomer);
    }

    @Transactional(readOnly = true)
    public Flux<UUID> getAllAccounts(UUID customerUuid) {
        return customerService.findCustomer(customerUuid)
            .flatMapMany(accountService::findByCustomer)
            .map(Account::getUuid);
    }

    @Transactional
    public Mono<ApiAccount> createAccount(UUID customerUuid) {
        return accountService.getNewAccountNumber()
            .zipWhen(num -> Mono.just(ibanService.generateIban(num)))
            .zipWith(Mono.just(Account.builder()), (values, builder) ->
                builder.accountNumber(values.getT1()).iban(values.getT2()))
            .zipWith(customerService.findCustomer(customerUuid), (builder, customer) -> builder.customerId(customer.getId()))
            .map(Account.AccountBuilder::build)
            .flatMap(accountService::createAccount)
            .zipWhen(balanceService::createForAccount, apiAccountMapper::toApiAccount);
    }
}
