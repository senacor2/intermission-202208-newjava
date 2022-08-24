package com.senacor.intermission.newjava.service;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {
/*
    @InjectMocks
    AccountService accountService;
    @Mock
    AccountRepository accountRepository;

    @Test
    public void createAccount() {
        Account account = Account.builder().build();
        accountService.createAccount(account);
        verify(accountRepository).save(account);
    }

    @Test
    public void getNewAccountNumber() {
        BigInteger accountNumber = BigInteger.valueOf(4711);
        doReturn(accountNumber).when(accountRepository).getNextAccountNumber();
        BigInteger result = accountService.getNewAccountNumber();
        assertThat(result).isEqualTo(accountNumber);
        verify(accountRepository).getNextAccountNumber();
    }

    @Test
    public void getAccount() {
        Account account = Account.builder().build();
        doReturn(Optional.of(account)).when(accountRepository).findByUuid(account.getUuid());
        Account result = accountService.getAccount(account.getUuid());
        assertThat(result).isEqualTo(account);
        verify(accountRepository).findByUuid(account.getUuid());
    }

    @Test
    public void getAccount_accountNotFound() {
        UUID accountUuid = UUID.randomUUID();
        doReturn(Optional.empty()).when(accountRepository).findByUuid(accountUuid);
        Throwable throwable = catchThrowable(() -> accountService.getAccount(accountUuid));
        assertThat(throwable).isInstanceOf(AccountNotFoundException.class);
        assertThat(throwable).hasMessage(MessageFormat.format("Account {0} not found!", accountUuid));
        verify(accountRepository).findByUuid(accountUuid);
    }

    @Test
    public void getAccountByIban() {
        Account account = Account.builder().iban("iban").build();
        doReturn(Optional.of(account)).when(accountRepository).findByIban("iban");
        Optional<Account> result = accountService.getAccountByIban("iban");
        assertThat(result.get()).isEqualTo(account);
        verify(accountRepository).findByIban("iban");
    }*/
}
