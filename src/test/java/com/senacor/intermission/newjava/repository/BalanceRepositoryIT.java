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

    private Customer customer;
    private Account account;

    @BeforeAll
    void setup() {
        this.customer = Customer.builder().build();
        this.account = Account.builder()
            .accountNumber(BigInteger.valueOf(40))
            .iban("iban")
            .customer(customer)
            .build();
        customer.addAccount(account);
        customerRepository.save(customer);
    }

    @AfterAll
    void cleanup() {
        customerRepository.delete(customer);
    }

    @Test
    @Order(1)
    void givenBalance__save__doesNotThrow() {
        Balance balance = Balance.builder().account(account).build();
        assertThatCode(() -> uut.save(balance)).doesNotThrowAnyException();
    }

    @Test
    @Order(2)
    void givenNewBalance__getByAccountIban__returnsBalanceWith0Cents() {
        Balance balance = uut.getByAccountIban("iban");
        assertThat(balance.getValueInCents()).isEqualTo(0);
        assertThat(balance.getLastUpdate()).isNotNull();
    }

}
