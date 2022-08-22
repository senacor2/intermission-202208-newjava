package com.senacor.intermission.newjava.service;

import com.senacor.intermission.newjava.model.Customer;
import com.senacor.intermission.newjava.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    public Customer createCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    public boolean deleteCustomer(UUID customerId) {
        Optional<Customer> customer = findCustomer(customerId);
        if (customer.isPresent()) {
            // TODO Anything else to delete?
            customerRepository.delete(customer.get());
            return true;
        }
        return false;
    }

    public Optional<Customer> findCustomer(UUID customerId) {
        Customer customer = customerRepository.getByUuid(customerId);
        return Optional.ofNullable(customer);
    }
}
