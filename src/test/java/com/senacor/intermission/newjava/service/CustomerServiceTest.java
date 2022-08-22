package com.senacor.intermission.newjava.service;

import com.senacor.intermission.newjava.model.Customer;
import com.senacor.intermission.newjava.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {

    @InjectMocks
    private CustomerService customerService;

    @Mock
    private CustomerRepository customerRepository;

    private final Customer customer = Customer.builder()
        .prename("Foo")
        .lastname("Bar")
        .dateOfBirth(LocalDate.of(1970, 1, 1))
        .build();

    @Test
    public void createCustomer() {
        Customer result = customerService.createCustomer(customer);
        verify(customerRepository).save(customer);
    }

    @Test
    public void deleteCustomer() {
        doReturn(customer).when(customerRepository).getByUuid(customer.getUuid());
        boolean result = customerService.deleteCustomer(customer.getUuid());
        assertThat(result).isTrue();
        verify(customerRepository).getByUuid(customer.getUuid());
        verify(customerRepository).delete(customer);
    }

    @Test
    public void deleteCustomer_notFound() {
        boolean result = customerService.deleteCustomer(customer.getUuid());
        assertThat(result).isFalse();
        verify(customerRepository).getByUuid(customer.getUuid());
    }

    @Test
    public void findCustomer() {
        doReturn(customer).when(customerRepository).getByUuid(customer.getUuid());
        Optional<Customer> result = customerService.findCustomer(customer.getUuid());
        assertThat(result.isPresent()).isTrue();
        verify(customerRepository).getByUuid(customer.getUuid());
    }

    @Test
    public void findCustomer_notFound() {
        doReturn(null).when(customerRepository).getByUuid(customer.getUuid());
        Optional<Customer> result = customerService.findCustomer(customer.getUuid());
        assertThat(result.isPresent()).isFalse();
        verify(customerRepository).getByUuid(customer.getUuid());
    }
}
