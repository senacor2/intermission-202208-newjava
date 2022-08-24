package com.senacor.intermission.newjava.service;

import com.senacor.intermission.newjava.exceptions.CustomerNotFoundException;
import com.senacor.intermission.newjava.model.Customer;
import com.senacor.intermission.newjava.repository.CustomerRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;

    @Transactional(propagation = Propagation.MANDATORY)
    public Mono<Customer> createCustomer(Customer customer) {
        if (!customer.isNew()) {
            throw new IllegalStateException("Customer is not new!");
        }
        return customerRepository.save(customer);
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public Mono<Void> deleteCustomer(Customer customer) {
        return customerRepository.delete(customer);
    }

    public Mono<Customer> findCustomer(UUID customerUuid) {
        return customerRepository.findByUuid(customerUuid)
            .switchIfEmpty(Mono.error(new CustomerNotFoundException(customerUuid)));
    }
}
