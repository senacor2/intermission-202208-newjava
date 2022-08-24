package com.senacor.intermission.newjava.repository;

import com.senacor.intermission.newjava.IntermissionNewJavaApplication;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
    classes = IntermissionNewJavaApplication.class,
    webEnvironment = SpringBootTest.WebEnvironment.NONE
)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TransactionRepositoryIT {
/*
    @Autowired
    TransactionRepository uut;

    @Autowired
    CustomerRepository customerRepository;

    private Customer customer;
    private Account account1;
    private Account account2;

    @BeforeAll
    void setup() {
        this.customer = Customer.builder().build();
        this.account1 = Account.builder()
            .accountNumber(BigInteger.valueOf(10))
            .iban("iban1")
            .customer(customer)
            .build();
        this.account2 = Account.builder()
            .accountNumber(BigInteger.valueOf(11))
            .iban("iban2")
            .customer(customer)
            .build();
        customer.addAccount(account1);
        customer.addAccount(account2);
        customerRepository.save(customer);
    }

    @AfterAll
    void cleanup() {
        customerRepository.delete(customer);
    }

    @Test
    void givenTransaction__save__doesNotThrow() {
        Transaction transaction = Transaction.builder()
            .valueInCents(BigInteger.ONE)
            .sender(account1)
            .receiver(account2)
            .build();
        Assertions.assertThatCode(() -> uut.save(transaction)).doesNotThrowAnyException();
    }*/

}
