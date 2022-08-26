package com.senacor.intermission.newjava.service;

import com.senacor.intermission.newjava.model.Account;
import com.senacor.intermission.newjava.model.Customer;
import com.senacor.intermission.newjava.model.Transaction;
import com.senacor.intermission.newjava.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PushNotificationService {

    private final TransactionRepository transactionRepository;
    private final DbTransactionalService dbTransactionalService;
    private final RestTemplateBuilder restTemplateBuilder;

    @Value("${app.pushserver.host:0}")
    private String pushserverHost;
    @Value("${app.pushserver.port:0}")
    private int pushserverPort;

    @Async
    public void sendPushNotification(UUID transactionUuid) {
        sendPushNotificationInternal(transactionUuid);
    }


    protected void sendPushNotificationInternal(UUID transactionUuid) {
        log.info("Prepare push notification ...");
        Optional<String> msg = dbTransactionalService.doTransactional(() -> createMessage(transactionUuid));
        if (msg.isPresent()) {
            log.info("Send push notifcation with message:\n" + msg.get());
            sendMessage(msg.get());
        } else {
            log.error("Transaction or account does not exist. No message was sent.");
        }
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

    private void sendMessage(String message) {
        RestTemplate template = restTemplateBuilder.build();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_PLAIN);

        HttpEntity<String> entity = new HttpEntity<>(message, headers);
        ResponseEntity<?> response = template
            .exchange("http://%s:%d/pushnotification".formatted(pushserverHost, pushserverPort),
                HttpMethod.POST,
                entity,
                String.class);
        HttpStatusCode status = response.getStatusCode();
        if (status.value() == 201) {
            log.info("... push notification sent.");
            return;
        }
        log.error("... push notification failed: %s".formatted(status));
    }
}
