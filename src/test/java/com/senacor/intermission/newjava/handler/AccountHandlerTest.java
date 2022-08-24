package com.senacor.intermission.newjava.handler;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class AccountHandlerTest {
    /*@InjectMocks
    private AccountHandler accountHandler;
    @Mock
    private AccountService accountService;
    @Spy
    private ApiAccountMapper apiAccountMapper = new ApiAccountMapperImpl();
    @Mock
    private TransactionService transactionService;
    @Spy
    private ApiTransactionMapper apiTransactionMapper = new ApiTransactionMapperImpl();

    @Test
    public void getAccount() {
        Account account = Account.builder().build();
        UUID accountUuid = account.getUuid();
        doReturn(account).when(accountService).getAccount(accountUuid);

        ApiAccount result = accountHandler.getAccount(accountUuid);
        assertThat(result.uuid()).isEqualTo(accountUuid);
        verify(accountService).getAccount(accountUuid);
    }

    @Test
    public void getAllTransactions() {
        Account account = Account.builder().build();
        UUID accountUuid = account.getUuid();
        doReturn(account).when(accountService).getAccount(accountUuid);
        Transaction transaction1 = Transaction.builder().build();
        Transaction transaction2 = Transaction.builder().build();
        doReturn(Arrays.asList(transaction1, transaction2)).when(transactionService).getTransactions(account);

        List<ApiTransaction> result = accountHandler.getAllTransactions(accountUuid);
        assertThat(result).hasSize(2);
        verify(accountService).getAccount(accountUuid);
        verify(transactionService).getTransactions(account);
    }

    @Test
    public void createTransaction() {
        Account account = Account.builder().iban("iban1").build();
        UUID accountUuid = account.getUuid();
        doReturn(account).when(accountService).getAccount(accountUuid);
        Account receiverAccount = Account.builder().iban("iban2").build();
        doReturn(Optional.of(receiverAccount)).when(accountService).getAccountByIban("iban2");
        ApiCreateTransaction createTransaction = new ApiCreateTransaction("iban2", null, null, null);
        doAnswer(invocation -> {
            Transaction transaction = invocation.getArgument(0);
            return transaction;
        }).when(transactionService).createTransaction(any());

        ApiTransaction result = accountHandler.createTransaction(accountUuid, createTransaction);
        assertThat(result.senderIban()).isEqualTo("iban1");
        assertThat(result.receiverIban()).isEqualTo("iban2");

        verify(accountService).getAccount(accountUuid);
        verify(accountService).getAccountByIban("iban2");
        verify(transactionService).createTransaction(any());
    }

    @Test
    public void createTransaction_receiverAccountNotFound() {
        Account account = Account.builder().iban("iban1").build();
        UUID accountUuid = account.getUuid();
        doReturn(account).when(accountService).getAccount(accountUuid);
        doReturn(Optional.empty()).when(accountService).getAccountByIban("iban2");
        ApiCreateTransaction createTransaction = new ApiCreateTransaction("iban2", null, null, null);

        Throwable throwable = Assertions.catchThrowable(() -> accountHandler.createTransaction(accountUuid, createTransaction));
        assertThat(throwable).isInstanceOf(IbanNotFoundException.class);
        assertThat(throwable).hasMessage("No account with iban iban2 found!");

        verify(accountService).getAccount(accountUuid);
        verify(accountService).getAccountByIban("iban2");
    }*/
}
