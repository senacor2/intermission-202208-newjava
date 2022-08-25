package com.senacor.intermission.newjava.repository;

import static org.assertj.core.api.Assertions.assertThatCode;

import com.senacor.intermission.newjava.IntermissionNewJavaApplication;
import com.senacor.intermission.newjava.model.Account;
import com.senacor.intermission.newjava.model.Customer;
import com.senacor.intermission.newjava.model.Transaction;
import java.math.BigInteger;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
    classes = IntermissionNewJavaApplication.class,
    webEnvironment = SpringBootTest.WebEnvironment.NONE
)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TransactionRepositoryIT {

    @Autowired
    TransactionRepository uut;

    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    AccountRepository accountRepository;

    private Customer customer;
    private Account account1;
    private Account account2;

    @BeforeAll
    void setup() {
        Mono<BigInteger> customerIdMono =
            Mono.just(Customer.builder().build())
                .flatMap(customerRepository::save)
                .doOnSuccess(it -> this.customer = it)
                .mapNotNull(Customer::getId);
        Flux.range(20, 2)
            .map(num -> Account.builder().accountNumber(BigInteger.valueOf(num)).iban("iban" + num))
            .zipWith(customerIdMono.cache().repeat(), Account.AccountBuilder::customerId)
            .map(Account.AccountBuilder::build)
            .flatMap(accountRepository::save)
            .collectList()
            .doOnSuccess(list -> account1 = list.get(0))
            .doOnSuccess(list -> account2 = list.get(1))
            .block();
    }

    @AfterAll
    void cleanup() {
        customerRepository.delete(customer).block();
    }

    @Test
    void givenTransaction__save__doesNotThrow() {
        Transaction transaction = Transaction.builder()
            .valueInCents(BigInteger.ONE)
            .senderId(account1.getId())
            .receiverId(account2.getId())
            .build();
        assertThatCode(() -> uut.save(transaction).block()).doesNotThrowAnyException();
    }

}
