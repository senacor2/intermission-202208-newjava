package com.senacor.intermission.newjava.handler;

import com.senacor.intermission.newjava.mapper.ApiAccountMapper;
import com.senacor.intermission.newjava.mapper.ApiAccountMapperImpl;
import com.senacor.intermission.newjava.mapper.ApiCustomerMapper;
import com.senacor.intermission.newjava.mapper.ApiCustomerMapperImpl;
import com.senacor.intermission.newjava.model.Account;
import com.senacor.intermission.newjava.model.Customer;
import com.senacor.intermission.newjava.model.api.ApiAccount;
import com.senacor.intermission.newjava.model.api.ApiCreateCustomer;
import com.senacor.intermission.newjava.model.api.ApiCustomer;
import com.senacor.intermission.newjava.service.AccountService;
import com.senacor.intermission.newjava.service.CustomerService;
import com.senacor.intermission.newjava.service.IbanService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigInteger;
import java.util.Collection;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.in;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class CustomerHandlerTest {
    @InjectMocks
    private CustomerHandler customerHandler;
    @Mock
    private CustomerService customerService;
    @Mock
    private AccountService accountService;
    @Mock
    private IbanService ibanService;
    @Spy
    private ApiCustomerMapper apiCustomerMapper = new ApiCustomerMapperImpl();
    @Spy
    private ApiAccountMapper apiAccountMapper = new ApiAccountMapperImpl();

    @Captor
    private ArgumentCaptor<Account> accountCaptor;

    @Test
    public void createCustomer() {
        ApiCreateCustomer createRequest = new ApiCreateCustomer();
        ApiCustomer result = customerHandler.createCustomer(createRequest);
        assertThat(result).isNotNull();
        verify(customerService).createCustomer(any());
    }

    @Test
    public void deleteCustomer() {
        Customer customer = Customer.builder().build();
        UUID customerUuid = customer.getUuid();
        doReturn(customer).when(customerService).findCustomer(customerUuid);

        customerHandler.deleteCustomer(customerUuid);
        verify(customerService).findCustomer(customerUuid);
        verify(customerService).deleteCustomer(customer);
    }

    @Test
    public void getAllAccounts() {
        Customer customer = Customer.builder().build();
        UUID customerUuid = customer.getUuid();
        Account account1 = Account.builder().customer(customer).build();
        Account account2 = Account.builder().customer(customer).build();
        Set<Account> accounts = customer.getAccounts();
        accounts.add(account1);
        accounts.add(account2);
        doReturn(customer).when(customerService).findCustomer(customerUuid);

        Collection<UUID> result = customerHandler.getAllAccounts(customerUuid);
        assertThat(result).containsExactlyInAnyOrder(account1.getUuid(), account2.getUuid());
        verify(customerService).findCustomer(customerUuid);
    }

    @Test
    public void createAccount() {
        Customer customer = Customer.builder().build();
        UUID customerUuid = customer.getUuid();
        doReturn(customer).when(customerService).findCustomer(customerUuid);
        BigInteger accountNumber = BigInteger.valueOf(4711);
        doReturn(accountNumber).when(accountService).getNewAccountNumber();
        String iban = "DE47111122220123456789";
        doReturn(iban).when(ibanService).generateIban(accountNumber);
        doAnswer(invocation -> {
            Account account = invocation.getArgument(0);
            return account;
        }).when(accountService).createAccount(any());

        ApiAccount result = customerHandler.createAccount(customerUuid);
        assertThat(result.getBalanceInCents()).isEqualTo(0L);
        assertThat(result.getIban()).isEqualTo(iban);

        verify(customerService).findCustomer(customerUuid);
        verify(accountService).getNewAccountNumber();
        verify(ibanService).generateIban(accountNumber);
        verify(accountService).createAccount(accountCaptor.capture());

        Account createdAccount = accountCaptor.getValue();
        assertThat(createdAccount.getCustomer()).isEqualTo(customer);
        assertThat(createdAccount.getAccountNumber()).isEqualTo(accountNumber);
        assertThat(createdAccount.getIban()).isEqualTo(iban);
        assertThat(createdAccount.getBalance().getValueInCents()).isEqualTo(BigInteger.ZERO);
    }
}
