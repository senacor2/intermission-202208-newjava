package com.senacor.intermission.newjava.service;

import com.senacor.intermission.newjava.exceptions.TransactionBookingFailedException;
import com.senacor.intermission.newjava.model.Balance;
import com.senacor.intermission.newjava.model.Transaction;
import com.senacor.intermission.newjava.model.enums.TransactionStatus;
import com.senacor.intermission.newjava.repository.BalanceRepository;
import com.senacor.intermission.newjava.repository.TransactionRepository;
import java.math.BigInteger;
import java.time.Clock;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionBookingService {

    private final Clock clock;
    private final TransactionalOperator transactionalOperator;
    private final TransactionRepository transactionRepository;
    private final BalanceRepository balanceRepository;

    @Value("${app.transaction-booking.batch-size}")
    private int batchSize;

    @Scheduled(fixedDelayString = "${app.transaction-booking.delay}")
    public void bookPendingTransactions() {
        log.debug("Started booking transactions...");
        Flux<BigInteger> transactions = transactionRepository.findAllByStatusAndTimeBefore(
            TransactionStatus.PENDING,
            LocalDateTime.now(clock),
            PageRequest.of(0, batchSize, Sort.Direction.ASC, "id")
        );
        Long count = transactions
            .flatMap(it -> transactionalOperator.execute(ignored -> bookSingleTransactionAsync(it)))
            .doOnError(ex -> log.warn("Booking of transaction failed!", ex))
            .count()
            .block();
        log.debug("Done booking {} transactions.", count);
    }

    private Publisher<Boolean> bookSingleTransactionAsync(BigInteger transactionId) {
        try {
            bookSingleTransaction(transactionId);
            return Mono.just(true);
        } catch (RuntimeException ex) {
            return Mono.error(ex);
        }
    }

    private void bookSingleTransaction(BigInteger transactionId) {
        Transaction transaction = transactionRepository.findById(transactionId)
            .blockOptional()
            .orElseThrow(TransactionBookingFailedException::new);

        Balance senderBalance = balanceRepository.getByAccountId(transaction.getSenderId())
            .blockOptional()
            .orElseThrow(TransactionBookingFailedException::new);
        Balance receiverBalance = balanceRepository.getByAccountId(transaction.getReceiverId())
            .blockOptional()
            .orElseThrow(TransactionBookingFailedException::new);

        BigInteger transactionAmount = transaction.getValueInCents();
        BigInteger senderBalanceAmountNew = senderBalance.getValueInCents().subtract(transactionAmount);
        BigInteger receiverBalanceAmountNew = receiverBalance.getValueInCents().add(transactionAmount);

        if (transactionAmount.compareTo(BigInteger.valueOf(1_000_000L)) > 0) {
            transaction.setStatus(TransactionStatus.REJECTED);
            transactionRepository.save(transaction).block();
            return;
        }
        if (senderBalanceAmountNew.signum() < 0 || receiverBalanceAmountNew.signum() < 0) {
            transaction.setStatus(TransactionStatus.REJECTED);
            transactionRepository.save(transaction).block();
            return;
        }

        senderBalance.setValueInCents(senderBalanceAmountNew);
        receiverBalance.setValueInCents(receiverBalanceAmountNew);
        transaction.setStatus(TransactionStatus.BOOKED);
        transaction.setTransactionTime(LocalDateTime.now(clock));


        transactionRepository.save(transaction)
            .and(balanceRepository.save(senderBalance))
            .and(balanceRepository.save(receiverBalance))
            .block();
    }


}
