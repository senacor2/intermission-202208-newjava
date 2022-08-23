package com.senacor.intermission.newjava.service;

import java.math.BigInteger;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class IbanService {

    private static final int PART_3_LEN = 8;
    private static final int PART_4_LEN = 10;

    @Value("${app.bic:0}")
    private String bic;

    public String generateIban(BigInteger accountNumber) {
        return "DE00"
            + prependZeroes(bic, PART_3_LEN)
            + prependZeroes(accountNumber.toString(), PART_4_LEN);
    }

    private String prependZeroes(String input, int expectedLength) {
        if (input.length() > expectedLength) throw new IllegalArgumentException();
        StringBuilder sb = new StringBuilder(expectedLength);
        Stream.iterate("0", z -> z).limit(expectedLength - input.length()).forEach(sb::append);
        sb.append(input);
        return sb.toString();
    }


}
