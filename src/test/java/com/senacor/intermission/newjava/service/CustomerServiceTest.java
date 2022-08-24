package com.senacor.intermission.newjava.service;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {
/*
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
    }*/
}
