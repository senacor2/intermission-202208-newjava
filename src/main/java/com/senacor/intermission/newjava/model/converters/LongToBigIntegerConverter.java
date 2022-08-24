package com.senacor.intermission.newjava.model.converters;

import java.math.BigInteger;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.stereotype.Component;

@ReadingConverter
@Component
public class LongToBigIntegerConverter implements DbConverter<Long, BigInteger> {
    @Override
    public BigInteger convert(Long source) {
        return BigInteger.valueOf(source);
    }
}
