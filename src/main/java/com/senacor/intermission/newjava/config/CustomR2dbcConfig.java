package com.senacor.intermission.newjava.config;

import com.senacor.intermission.newjava.model.converters.DbConverter;
import io.r2dbc.spi.ConnectionFactory;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;

@RequiredArgsConstructor
@Configuration
public class CustomR2dbcConfig extends AbstractR2dbcConfiguration {

    private final ConnectionFactory connectionFactory;
    private final Collection<DbConverter<?, ?>> dbConverters;

    @Override
    public ConnectionFactory connectionFactory() {
        return connectionFactory;
    }

    @Override
    protected List<Object> getCustomConverters() {
        return new ArrayList<>(dbConverters);
    }
}
