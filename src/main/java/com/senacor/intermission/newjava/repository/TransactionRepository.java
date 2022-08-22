package com.senacor.intermission.newjava.repository;

import com.senacor.intermission.newjava.model.Transaction;
import java.math.BigInteger;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, BigInteger> {
}
