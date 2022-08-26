package com.senacor.intermission.newjava.controller;

import com.senacor.intermission.newjava.model.Account;
import com.senacor.intermission.newjava.model.Balance;
import com.senacor.intermission.newjava.model.Customer;
import com.senacor.intermission.newjava.repository.AccountRepository;
import com.senacor.intermission.newjava.repository.BalanceRepository;
import com.senacor.intermission.newjava.repository.CustomerRepository;
import java.math.BigInteger;
import java.time.LocalDateTime;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AccountControllerIT {

    @Autowired
    private WebTestClient client;

    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    BalanceRepository balanceRepository;

    private Customer customer;
    private Account account1;
    private Account account2;

    @BeforeAll
    void setup() {
        this.customer = Customer.builder().build();
        customerRepository.save(customer).block();
        this.account1 = Account.builder()
            .customerId(customer.getId())
            .accountNumber(BigInteger.valueOf(4711))
            .iban("DE00123456780000004711")
            .build();
        accountRepository.save(account1).block();
        Balance balance = Balance.builder()
            .accountId(account1.getId())
            .valueInCents(BigInteger.valueOf(20000)).build();
        balanceRepository.save(balance).block();

        this.account2 = Account.builder()
            .customerId(customer.getId())
            .accountNumber(BigInteger.valueOf(4812))
            .iban("DE00123456780000004812")
            .build();
        accountRepository.save(account2).block();
        balance = Balance.builder()
            .accountId(account2.getId())
            .valueInCents(BigInteger.ZERO).build();
        balanceRepository.save(balance).block();
    }

    @AfterAll
    void cleanup() {
        accountRepository.delete(account1).block();
        customerRepository.delete(customer).block();
    }

    @Test
    @Order(1)
    public void getAccount() throws Exception {
        client
            .get()
                .uri("/accounts/{uuid}", account1.getUuid())
                .accept(MediaType.APPLICATION_JSON)
            .exchange()
                .expectStatus().isOk()
                .expectBody()
                    .jsonPath("$.accountNumber").isEqualTo(account1.getAccountNumber().longValue())
                    .jsonPath("$.iban").isEqualTo(account1.getIban())
                    .jsonPath("$.uuid").isEqualTo(account1.getUuid().toString())
                    .jsonPath("$.balanceInCents").isEqualTo("20000");
    }

    @Test
    @Order(2)
    public void createTransaction() throws Exception {
        client
            .post()
                .uri("/accounts/{uuid}/transactions", account1.getUuid())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue("""
                    {
                        "amountInCents": 4999,
                        "description": "Almost 50 Euros",
                        "receiverIban": "%s",
                        "transactionDate": "%s"
                    }""".formatted(account2.getIban(), LocalDateTime.now())
                )
            .exchange()
                .expectStatus().isCreated()
                .expectBody()
                    .jsonPath("$.senderIban").isEqualTo(account1.getIban())
                    .jsonPath("$.receiverIban").isEqualTo(account2.getIban())
                    .jsonPath("$.amountInCents").isEqualTo("4999")
                    .jsonPath("$.transactionDate").exists()
                    .jsonPath("$.description").isEqualTo("Almost 50 Euros")
                    .jsonPath("$.status").isEqualTo("PENDING");
    }


    @Test
    @Order(3)
    public void createTransaction_invalidRequest() throws Exception {
        client
            .post()
                .uri("/accounts/{uuid}/transactions", account1.getUuid())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue("""
                    {
                        "amountInCents": 4999,
                        "description": "Almost 50 Euros",
                        "transactionDate": "%s"
                    }""".formatted(LocalDateTime.now())
                )
            .exchange()
                .expectStatus().isBadRequest();
    }


    @Test
    @Order(4)
    public void createInstantTransaction() throws Exception {
        client
            .post()
                .uri("/accounts/{uuid}/transactions/instant", account1.getUuid())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue("""
                    {
                        "amountInCents": 4999,
                        "description": "Almost 50 Euros",
                        "receiverIban": "%s",
                        "transactionDate": "%s"
                    }""".formatted(account2.getIban(), LocalDateTime.now())
                )
            .exchange()
                .expectStatus().isCreated()
                .expectBody()
                    .jsonPath("$.senderIban").isEqualTo(account1.getIban())
                    .jsonPath("$.receiverIban").isEqualTo(account2.getIban())
                    .jsonPath("$.amountInCents").isEqualTo("4999")
                    .jsonPath("$.transactionDate").exists()
                    .jsonPath("$.description").isEqualTo("Almost 50 Euros")
                    .jsonPath("$.status").isEqualTo("BOOKED");
    }

    @Test
    @Order(5)
    public void getAllTransactions() throws Exception {
        client
            .get()
                .uri("/accounts/{uuid}/transactions", account1.getUuid())
                .accept(MediaType.APPLICATION_JSON)
            .exchange()
                .expectStatus().isOk()
                .expectBody()
                    .jsonPath("$[0].description").exists()
                    .jsonPath("$[1].description").exists();
    }
}
