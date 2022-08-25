package com.senacor.intermission.newjava.handler;

import com.senacor.intermission.newjava.exceptions.IbanNotFoundException;
import com.senacor.intermission.newjava.mapper.ApiAccountMapper;
import com.senacor.intermission.newjava.mapper.ApiAccountMapperImpl;
import com.senacor.intermission.newjava.mapper.ApiTransactionMapper;
import com.senacor.intermission.newjava.mapper.ApiTransactionMapperImpl;
import com.senacor.intermission.newjava.model.Account;
import com.senacor.intermission.newjava.model.Transaction;
import com.senacor.intermission.newjava.model.api.ApiAccount;
import com.senacor.intermission.newjava.model.api.ApiCreateTransaction;
import com.senacor.intermission.newjava.model.api.ApiTransaction;
import com.senacor.intermission.newjava.service.AccountService;
import com.senacor.intermission.newjava.service.PushNotificationService;
import com.senacor.intermission.newjava.service.TransactionService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccountHandlerTest {
    @InjectMocks
    private AccountHandler accountHandler;
    @Mock
    private AccountService accountService;
    @Spy
    private ApiAccountMapper apiAccountMapper = new ApiAccountMapperImpl();
    @Mock
    private TransactionService transactionService;
    @Spy
    private ApiTransactionMapper apiTransactionMapper = new ApiTransactionMapperImpl();
    @Mock
    private PushNotificationService pushNotificationService;

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
        verify(pushNotificationService).sendPushNotification(any());
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
    }
}
