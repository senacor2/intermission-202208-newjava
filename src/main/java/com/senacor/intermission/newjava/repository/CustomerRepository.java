package com.senacor.intermission.newjava.repository;

import com.senacor.intermission.newjava.model.Customer;
import java.math.BigInteger;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, BigInteger> {
}
