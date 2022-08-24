package com.senacor.intermission.newjava.model.converters;

import java.math.BigInteger;
import org.springframework.data.convert.WritingConverter;
import org.springframework.stereotype.Component;

@WritingConverter
@Component
public class BigIntegerToLongConverter implements DbConverter<BigInteger, Long> {
    @Override
    public Long convert(BigInteger source) {
        return source.longValueExact();
    }
}
