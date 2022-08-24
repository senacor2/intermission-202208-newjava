package com.senacor.intermission.newjava.service;

import com.senacor.intermission.newjava.model.Account;
import com.senacor.intermission.newjava.model.Balance;
import com.senacor.intermission.newjava.repository.BalanceRepository;
import java.math.BigInteger;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class BalanceService {

    private final BalanceRepository balanceRepository;

    public Mono<Balance> getBalanceForAccount(Account account) {
        return balanceRepository.getByAccountId(account.getId());
    }

    public Mono<Balance> createForAccount(Account account) {
        var balance = Balance.builder()
            .valueInCents(BigInteger.ZERO)
            .accountId(account.getId())
            .build();
        return balanceRepository.save(balance);
    }

}
