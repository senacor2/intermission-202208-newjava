package com.senacor.intermission.newjava.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import com.senacor.intermission.newjava.IntermissionNewJavaApplication;
import com.senacor.intermission.newjava.model.Customer;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
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
public class CustomerRepositoryIT {

    @Autowired
    CustomerRepository uut;

    private final Set<Customer> cleanup = new HashSet<>();
    private UUID customerUuid;


    @AfterAll
    void cleanup() {
        uut.deleteAll(cleanup).block();
    }

    @Test
    @Order(1)
    void givenCustomer__save__doesNotThrow() {
        Customer customer = Customer.builder()
            .prename("foo")
            .lastname("bar")
            .build();
        assertThatCode(() -> uut.save(customer).block()).doesNotThrowAnyException();
        this.customerUuid = customer.getUuid();
        cleanup.add(customer);
    }

    @Test
    @Order(2)
    void givenCustomer__findByUuid() {
        assertThat(uut.findByUuid(customerUuid).block())
            .isNotNull()
            .returns("foo", Customer::getPrename)
            .returns("bar", Customer::getLastname);
    }
}
