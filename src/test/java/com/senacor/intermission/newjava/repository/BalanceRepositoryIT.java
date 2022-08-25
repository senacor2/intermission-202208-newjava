package com.senacor.intermission.newjava.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import com.senacor.intermission.newjava.IntermissionNewJavaApplication;
import com.senacor.intermission.newjava.model.Account;
import com.senacor.intermission.newjava.model.Balance;
import com.senacor.intermission.newjava.model.Customer;
import java.math.BigInteger;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Mono;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
    classes = IntermissionNewJavaApplication.class,
    webEnvironment = SpringBootTest.WebEnvironment.NONE
)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BalanceRepositoryIT {

    @Autowired
    BalanceRepository uut;

    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    AccountRepository accountRepository;

    private Customer customer;
    private Account account;

    @BeforeAll
    void setup() {
        Mono.just(Customer.builder().build())
            .flatMap(customerRepository::save)
            .doOnSuccess(customer -> this.customer = customer)
            .map(customer -> Account.builder()
                .customerId(customer.getId())
                .accountNumber(BigInteger.valueOf(40))
                .iban("iban")
                .build())
            .flatMap(accountRepository::save)
            .doOnSuccess(account -> this.account = account)
            .block();
    }

    @AfterAll
    void cleanup() {
        customerRepository.delete(customer).block();
    }

    @Test
    @Order(1)
    void givenBalance__save__doesNotThrow() {
        Balance balance = Balance.builder().accountId(account.getId()).build();
        assertThatCode(() -> uut.save(balance).block()).doesNotThrowAnyException();
    }

    @Test
    @Order(2)
    void givenNewBalance__getByAccountIban__returnsBalanceWith0Cents() {
        Balance balance = uut.getByAccountIban("iban").block();
        assertThat(balance).isNotNull();
        assertThat(balance.getValueInCents()).isEqualTo(0);
        assertThat(balance.getLastUpdate()).isNotNull();
    }

}
