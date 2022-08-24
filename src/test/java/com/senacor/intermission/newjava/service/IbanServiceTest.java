package com.senacor.intermission.newjava.service;

import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigInteger;

import static org.assertj.core.api.Assertions.assertThat;

public class IbanServiceTest {

    private IbanService ibanService = new IbanService();

    @Test
    public void generateIban() {
        ReflectionTestUtils.setField(ibanService, "bic", "12345678");

        BigInteger accountNumber = BigInteger.valueOf(4711);
        String iban = ibanService.generateIban(accountNumber);
        assertThat(iban).isEqualTo("DE08" + "12345678" + "0000004711");
    }
}
