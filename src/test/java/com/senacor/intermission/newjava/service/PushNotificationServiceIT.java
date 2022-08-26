package com.senacor.intermission.newjava.service;

import com.senacor.intermission.newjava.IntermissionNewJavaApplication;
import com.senacor.intermission.newjava.model.Account;
import com.senacor.intermission.newjava.model.BaseCustomer;
import com.senacor.intermission.newjava.model.Customer;
import com.senacor.intermission.newjava.model.Transaction;
import com.senacor.intermission.newjava.repository.AccountRepository;
import com.senacor.intermission.newjava.repository.CustomerRepository;
import com.senacor.intermission.newjava.repository.TransactionRepository;
import org.junit.Rule;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockserver.client.MockServerClient;
import org.mockserver.model.StringBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.testcontainers.containers.MockServerContainer;
import org.testcontainers.utility.DockerImageName;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;


@ExtendWith(SpringExtension.class)
@SpringBootTest(
    classes = IntermissionNewJavaApplication.class,
    webEnvironment = SpringBootTest.WebEnvironment.NONE
)
public class PushNotificationServiceIT {

    @Autowired
    PushNotificationService pushNotificationService;

    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    TransactionRepository transactionRepository;

    @Rule
    public MockServerContainer mockServer = new MockServerContainer(DockerImageName.
        parse("mockserver/mockserver").
        withTag("mockserver-5.10.0"));

    private Customer customer;
    private final String accountIban1 = "DE12345678901234567890";
    private final String accountIban2 = "DE12345678901234567891";
    private UUID transactionUuid;

    @BeforeEach
    public void setup() {
        mockServer.start();
        ReflectionTestUtils.setField(pushNotificationService, "pushserverHost", mockServer.getHost());
        ReflectionTestUtils.setField(pushNotificationService, "pushserverPort", mockServer.getServerPort());

        this.customer = BaseCustomer.builder()
            .prename("Mister")
            .lastname("Knister")
            .build();
        customerRepository.save(customer);

        Account account1 = Account.builder()
            .customer(customer)
            .accountNumber(BigInteger.valueOf(1234567890))
            .iban(accountIban1)
            .build();
        accountRepository.save(account1);

        Account account2 = Account.builder()
            .customer(customer)
            .accountNumber(BigInteger.valueOf(1234567891))
            .iban(accountIban2)
            .build();
        accountRepository.save(account2);

        Transaction transaction = Transaction.builder()
            .sender(account1)
            .receiver(account2)
            .valueInCents(BigInteger.valueOf(1000))
            .description("Something for you")
            .transactionTime(LocalDateTime.now())
            .build();
        transactionRepository.save(transaction);
        transactionUuid = transaction.getUuid();
    }

    @AfterEach
    public void tearDown() {
        mockServer.stop();
    }

    @Test
    public void sendPushNotification() {
        /* Mock the expected call. */
        new MockServerClient(mockServer.getHost(), mockServer.getServerPort())
            .when(
                request()
                    .withMethod("POST")
                    .withPath("/pushnotification")
                    .withHeader("Content-type", "text/plain")
                    .withBody(StringBody.exact("""
                        Dear Mister Knister,
                        A new transaction of 10,00 EUROs has been issued for your account.
                        Description: Something for you
                        """))
            )
            .respond(
                response()
                    .withStatusCode(201)
                    .withDelay(TimeUnit.SECONDS, 5)
            );

        pushNotificationService.sendPushNotificationInternal(transactionUuid);
    }
}
