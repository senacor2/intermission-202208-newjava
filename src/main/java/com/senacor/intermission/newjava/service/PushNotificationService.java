package com.senacor.intermission.newjava.service;

import com.senacor.intermission.newjava.model.Account;
import com.senacor.intermission.newjava.model.Customer;
import com.senacor.intermission.newjava.model.Transaction;
import com.senacor.intermission.newjava.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PushNotificationService {

    private final TransactionRepository transactionRepository;
    private final DbTransactionalService dbTransactionalService;

    @Async
    public void sendPushNotification(UUID transactionUuid) {
        log.info("Prepare push notification ...");
        Optional<String> msg = dbTransactionalService.doTransactional(() -> createMessage(transactionUuid));
        if (msg.isPresent()) {
            log.info("Send push notifcation with message:\n" + msg.get());
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                log.warn("... due to an error, no message was sent.");
            }
        } else {
            log.error("Transaction or account does not exist. No message was sent.");
        }
        log.info("... push notification sent.");
    }

    private Optional<String> createMessage(UUID transactionUuid) {
        Optional<Transaction> transaction = transactionRepository.findByUuid(transactionUuid);
        Optional<Customer> customer = transaction.map(Transaction::getReceiver)
            .map(Account::getCustomer);
        if (!customer.isPresent()) {
            return Optional.empty();
        }

        String msg = """
            Dear %s %s,
            A new transaction of %.2f EUROs has been issued for your account.
            Description: %s
            """.formatted(customer.get().getPrename(),
            customer.get().getLastname(),
            BigDecimal.valueOf(transaction.get().getValueInCents().longValue()).divide(BigDecimal.valueOf(100)),
            transaction.get().getDescription());
        return Optional.of(msg);
    }
}
