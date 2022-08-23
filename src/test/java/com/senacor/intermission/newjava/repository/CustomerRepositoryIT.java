package com.senacor.intermission.newjava.repository;

import com.senacor.intermission.newjava.IntermissionNewJavaApplication;
import com.senacor.intermission.newjava.model.Customer;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.HashSet;
import java.util.Set;

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

    @Test
    void givenCustomer__findByUuid() {
        Customer customer = Customer.builder().build();
        uut.save(customer);
        Assertions.assertThat(uut.findByUuid(customer.getUuid()).get()).isEqualTo(customer);
        cleanup.add(customer);
    }
}
