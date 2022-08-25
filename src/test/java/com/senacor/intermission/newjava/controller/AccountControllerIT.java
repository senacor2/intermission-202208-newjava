package com.senacor.intermission.newjava.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AccountControllerIT {

    @Autowired
    private MockMvc mvc;
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
        MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/accounts/" + account1.getUuid())
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8"))
            .andExpect(request().asyncStarted())
            .andReturn();
        mvc.perform(asyncDispatch(result))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.accountNumber").value(account1.getAccountNumber().longValue()))
            .andExpect(jsonPath("$.iban").value(account1.getIban()))
            .andExpect(jsonPath("$.uuid").value(account1.getUuid().toString()))
            .andExpect(jsonPath("$.balanceInCents").value("20000"));
    }

    @Test
    @Order(2)
    public void createTransaction() throws Exception {
        MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/accounts/" + account1.getUuid()
                    + "/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content("""
                    {
                        "amountInCents": 4999,
                        "description": "Almost 50 Euros",
                        "receiverIban": "%s",
                        "transactionDate": "%s"
                    }""".formatted(account2.getIban(), LocalDateTime.now())
                ))
            .andExpect(request().asyncStarted())
            .andReturn();
        mvc.perform(asyncDispatch(result))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.senderIban").value(account1.getIban()))
            .andExpect(jsonPath("$.receiverIban").value(account2.getIban()))
            .andExpect(jsonPath("$.amountInCents").value("4999"))
            .andExpect(jsonPath("$.transactionDate").exists())
            .andExpect(jsonPath("$.description").value("Almost 50 Euros"))
            .andExpect(jsonPath("$.status").value("PENDING"));
    }


    @Test
    @Order(3)
    public void createTransaction_invalidRequest() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/accounts/" + account1.getUuid()
                    + "/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content("""
                    {
                        "amountInCents": 4999,
                        "description": "Almost 50 Euros",
                        "transactionDate": "%s"
                    }""".formatted(LocalDateTime.now())
                ))
            .andExpect(status().isBadRequest());
    }


    @Test
    @Order(4)
    public void createInstantTransaction() throws Exception {
        MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/accounts/" + account1.getUuid()
                    + "/transactions/instant")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content("""
                    {
                        "amountInCents": 5999,
                        "description": "ASAP",
                        "receiverIban": "%s",
                        "transactionDate": "%s"
                    }""".formatted(account2.getIban(), LocalDateTime.now())
                ))
            .andExpect(request().asyncStarted())
            .andReturn();
        mvc.perform(asyncDispatch(result))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.senderIban").value(account1.getIban()))
            .andExpect(jsonPath("$.receiverIban").value(account2.getIban()))
            .andExpect(jsonPath("$.amountInCents").value("5999"))
            .andExpect(jsonPath("$.transactionDate").exists())
            .andExpect(jsonPath("$.description").value("ASAP"))
            .andExpect(jsonPath("$.status").value("BOOKED"));
    }

    @Test
    @Order(5)
    public void getAllTransactions() throws Exception {
        MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/accounts/" + account1.getUuid()
                    + "/transactions")
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8"))
            .andExpect(request().asyncStarted())
            .andReturn();
        mvc.perform(asyncDispatch(result))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].description").exists())
            .andExpect(jsonPath("$[1].description").exists());
    }
}
