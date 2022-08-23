package com.senacor.intermission.newjava.service;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class IbanService {

    private static final int PART_4_LEN = 10;

    @Value("${app.bic}")
    private String bic;

    public String generateIban(BigInteger accountNumber) {
        return "DE0000000000" + prependZeroes(accountNumber.toString(), PART_4_LEN);
    }

    private String prependZeroes(String input, int expectedLength) {
        if (input.length() > expectedLength) throw new IllegalArgumentException();
        StringBuilder sb = new StringBuilder(expectedLength);
        Stream.of("0").limit(expectedLength - input.length()).forEach(sb::append);
        sb.append(input);
        return sb.toString();
    }


}
