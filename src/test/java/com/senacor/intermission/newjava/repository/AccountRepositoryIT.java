package com.senacor.intermission.newjava.repository;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.senacor.intermission.newjava.IntermissionNewJavaApplication;
import com.senacor.intermission.newjava.model.Account;
import com.senacor.intermission.newjava.model.Customer;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
    classes = IntermissionNewJavaApplication.class,
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AccountRepositoryIT {

    @Autowired
    AccountRepository uut;

    @Autowired
    CustomerRepository customerRepository;

    private Customer customer;
    private String accountIban = "DE12345678901234567890";

    @BeforeAll
    void setup() {
        this.customer = Customer.builder().build();
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