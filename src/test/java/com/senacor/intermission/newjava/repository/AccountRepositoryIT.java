package com.senacor.intermission.newjava.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.senacor.intermission.newjava.IntermissionNewJavaApplication;
import com.senacor.intermission.newjava.model.Account;
import com.senacor.intermission.newjava.model.BaseCustomer;
import com.senacor.intermission.newjava.model.Customer;
import java.math.BigInteger;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
    classes = IntermissionNewJavaApplication.class,
    webEnvironment = SpringBootTest.WebEnvironment.NONE,
    properties = {
        "spring.datasource.url=jdbc:tc:postgresql:14-alpine://newjava",
        "spring.flyway.locations=classpath:db.postgres"
    }
)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AccountRepositoryIT {

    @Autowired
    AccountRepository uut;

    @Autowired
    CustomerRepository customerRepository;

    private Customer customer;
    private final String accountIban = "DE12345678901234567890";

    @BeforeAll
    void setup() {
        this.customer = BaseCustomer.builder().build();
        customerRepository.save(customer);
    }

    @AfterAll
    void cleanup() {
        customerRepository.delete(customer);
    }

    @Test
    @Order(1)
    void givenAccount__save__doesNotThrow() {
        Account account = Account.builder()
            .accountNumber(BigInteger.valueOf(20))
            .iban(accountIban)
            .customer(customer)
            .build();
        Assertions.assertThatCode(() -> uut.save(account)).doesNotThrowAnyException();
    }

    @Test
    @Order(2)
    void givenExistingIban__findByIban__returnsAccount() {
        assertThat(uut.findByIban(accountIban))
            .isPresent()
            .get()
            .returns(customer, Account::getCustomer);
    }

    @Test
    @Order(3)
    void givenUnknownIban__findByIban__returnsEmpty() {
        assertThat(uut.findByIban("unknown"))
            .isEmpty();
    }
}
