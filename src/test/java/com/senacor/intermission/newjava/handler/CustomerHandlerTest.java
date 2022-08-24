package com.senacor.intermission.newjava.handler;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CustomerHandlerTest {
    /*@InjectMocks
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
        ApiCreateCustomer createRequest = new ApiCreateCustomer(null, null, null);
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
        assertThat(result.balanceInCents()).isEqualTo(0L);
        assertThat(result.iban()).isEqualTo(iban);

        verify(customerService).findCustomer(customerUuid);
        verify(accountService).getNewAccountNumber();
        verify(ibanService).generateIban(accountNumber);
        verify(accountService).createAccount(accountCaptor.capture());

        Account createdAccount = accountCaptor.getValue();
        assertThat(createdAccount.getCustomer()).isEqualTo(customer);
        assertThat(createdAccount.getAccountNumber()).isEqualTo(accountNumber);
        assertThat(createdAccount.getIban()).isEqualTo(iban);
        assertThat(createdAccount.getBalance().getValueInCents()).isEqualTo(BigInteger.ZERO);
    }*/
}
