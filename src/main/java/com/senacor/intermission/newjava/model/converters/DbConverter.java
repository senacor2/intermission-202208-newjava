package com.senacor.intermission.newjava.model.converters;

import org.springframework.core.convert.converter.Converter;

public interface DbConverter<S, T> extends Converter<S, T> {
}
