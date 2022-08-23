package com.senacor.intermission.newjava.service;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

@Service
@RequiredArgsConstructor
public class DbTransactionalService {

    private final PlatformTransactionManager platformTransactionManager;

    public <T, R> Function<T, R> transactional(Function<T, R> task) {
        TransactionTemplate transactionTemplate = new TransactionTemplate(platformTransactionManager);
        transactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        return arg -> transactionTemplate.execute(ignored -> task.apply(arg));
    }

    public <T> Consumer<T> transactional(Consumer<T> task) {
        TransactionTemplate transactionTemplate = new TransactionTemplate(platformTransactionManager);
        transactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        return arg -> transactionTemplate.executeWithoutResult(ignored -> task.accept(arg));
    }

    public <R> Supplier<R> transactional(Supplier<R> task) {
        TransactionTemplate transactionTemplate = new TransactionTemplate(platformTransactionManager);
        transactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        return () -> transactionTemplate.execute(ignored -> task.get());
    }

    public <R> R doTransactional(Supplier<R> task) {
        return transactional(task).get();
    }

    public void doTransactional(Runnable task) {
        TransactionTemplate transactionTemplate = new TransactionTemplate(platformTransactionManager);
        transactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        transactionTemplate.executeWithoutResult(ignored -> task.run());
    }

}
