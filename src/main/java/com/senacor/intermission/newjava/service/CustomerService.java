package com.senacor.intermission.newjava.service;

import com.senacor.intermission.newjava.exceptions.CustomerNotFoundException;
import com.senacor.intermission.newjava.model.Customer;
import com.senacor.intermission.newjava.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private CustomerRepository customerRepository;

    @Transactional(propagation = Propagation.MANDATORY)
    public Customer createCustomer(Customer customer) {
        if (!customer.isNew()) {
            throw new IllegalStateException("Customer is not new!");
        }
        return customerRepository.save(customer);
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public void deleteCustomer(Customer customer) {
        customerRepository.delete(customer);
    }

    public Customer findCustomer(UUID customerUuid) {
        return customerRepository.findByUuid(customerUuid)
            .orElseThrow(() -> new CustomerNotFoundException(customerUuid));
    }
}
