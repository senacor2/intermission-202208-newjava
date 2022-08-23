package com.senacor.intermission.newjava.repository;

import com.senacor.intermission.newjava.model.Balance;
import com.senacor.intermission.newjava.model.Customer;
import java.math.BigInteger;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, BigInteger> {

    Optional<Customer> findByUuid(UUID uuid);
}
