package com.senacor.intermission.newjava.service;

import com.senacor.intermission.newjava.exceptions.TransactionBookingFailedException;
import com.senacor.intermission.newjava.model.Balance;
import com.senacor.intermission.newjava.model.Transaction;
import com.senacor.intermission.newjava.model.enums.TransactionStatus;
import com.senacor.intermission.newjava.repository.BalanceRepository;
import com.senacor.intermission.newjava.repository.TransactionRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import java.math.BigInteger;
import java.time.Clock;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionOperations;
import org.springframework.transaction.support.TransactionTemplate;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionBookingService {

    private final Clock clock;
    private final PlatformTransactionManager platformTransactionManager;
    private final TransactionRepository transactionRepository;
    private final BalanceRepository balanceRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Value("${app.transaction-booking.batch-size}")
    private int batchSize;

    @Scheduled(fixedDelayString = "${app.transaction-booking.delay}")
    @Transactional(propagation = Propagation.NEVER)
    public void bookPendingTransactions() {
        log.debug("Started booking transactions...");
        Page<BigInteger> transactions = transactionRepository.findAllByStatusAndTimeBefore(
            TransactionStatus.PENDING,
            LocalDateTime.now(clock),
            PageRequest.of(0, batchSize, Sort.Direction.ASC, "id")
        );
        log.debug("Found {} transactions to book...", transactions.getNumberOfElements());
        for (BigInteger transactionId : transactions) {
            try {
                prepareNestedDbTransaction()
                    .executeWithoutResult(ignored -> bookSingleTransaction(transactionId));
            } catch (RuntimeException ex) {
                log.warn("Booking of transaction {} failed!", transactionId, ex);
            }
        }
        log.debug("Done booking {} transactions.", transactions.getNumberOfElements());
    }

    private void bookSingleTransaction(BigInteger transactionId) {
        Transaction transaction = transactionRepository.findById(transactionId)
            .orElseThrow(TransactionBookingFailedException::new);
        entityManager.lock(transaction, LockModeType.PESSIMISTIC_WRITE);

        Balance senderBalance = balanceRepository.getByAccount(transaction.getSender());
        Balance receiverBalance = balanceRepository.getByAccount(transaction.getReceiver());

        BigInteger transactionAmount = transaction.getValueInCents();
        BigInteger senderBalanceAmountNew = senderBalance.getValueInCents().subtract(transactionAmount);
        BigInteger receiverBalanceAmountNew = receiverBalance.getValueInCents().add(transactionAmount);

        if (transactionAmount.compareTo(BigInteger.valueOf(1_000_000L)) > 0) {
            transaction.setStatus(TransactionStatus.REJECTED);
            return;
        }
        if (senderBalanceAmountNew.signum() < 0 || receiverBalanceAmountNew.signum() < 0) {
            transaction.setStatus(TransactionStatus.REJECTED);
            return;
        }

        senderBalance.setValueInCents(senderBalanceAmountNew);
        receiverBalance.setValueInCents(receiverBalanceAmountNew);
        transaction.setStatus(TransactionStatus.BOOKED);
        transaction.setTransactionTime(LocalDateTime.now(clock));
    }

    private TransactionOperations prepareNestedDbTransaction() {
        TransactionTemplate tx = new TransactionTemplate(platformTransactionManager);
        tx.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        tx.setReadOnly(false);
        return tx;
    }


}
