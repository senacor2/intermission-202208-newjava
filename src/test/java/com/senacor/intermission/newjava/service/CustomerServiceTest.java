package com.senacor.intermission.newjava.service;

import com.senacor.intermission.newjava.exceptions.CustomerNotFoundException;
import com.senacor.intermission.newjava.model.Customer;
import com.senacor.intermission.newjava.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.text.MessageFormat;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

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
        customerService.deleteCustomer(customer);
        verify(customerRepository).delete(customer);
    }

    @Test
    public void findCustomer() {
        doReturn(Optional.of(customer)).when(customerRepository).findByUuid(customer.getUuid());
        Customer result = customerService.findCustomer(customer.getUuid());
        assertThat(result).isEqualTo(customer);
        verify(customerRepository).findByUuid(customer.getUuid());
    }

    @Test
    public void findCustomer_notFound() {
        doReturn(Optional.empty()).when(customerRepository).findByUuid(customer.getUuid());
        assertThatThrownBy(() -> customerService.findCustomer(customer.getUuid()))
            .isInstanceOf(CustomerNotFoundException.class)
            .hasMessage(MessageFormat.format("Customer {0} not found!", customer.getUuid()));
        verify(customerRepository).findByUuid(customer.getUuid());
    }
}
