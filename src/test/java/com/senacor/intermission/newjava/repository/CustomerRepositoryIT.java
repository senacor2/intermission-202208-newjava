package com.senacor.intermission.newjava.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.senacor.intermission.newjava.IntermissionNewJavaApplication;
import com.senacor.intermission.newjava.model.Account;
import com.senacor.intermission.newjava.model.Customer;
import java.util.HashSet;
import java.util.Set;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
    classes = IntermissionNewJavaApplication.class,
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CustomerRepositoryIT {

    @Autowired
    CustomerRepository uut;

    private final Set<Customer> cleanup = new HashSet<>();

    @AfterAll
    void cleanup() {
        uut.deleteAll(cleanup);
    }

    @Test
    void givenCustomer__save__doesNotThrow() {
        Customer customer = Customer.builder().build();
        Assertions.assertThatCode(() -> uut.save(customer)).doesNotThrowAnyException();
        cleanup.add(customer);
    }

}