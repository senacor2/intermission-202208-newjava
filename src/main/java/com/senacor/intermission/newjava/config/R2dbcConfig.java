package com.senacor.intermission.newjava.config;

import io.r2dbc.h2.H2ConnectionConfiguration;
import io.r2dbc.h2.H2ConnectionFactory;
import io.r2dbc.spi.ConnectionFactory;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class R2dbcConfig {

    @Value("${spring.datasource.name}")
    private String datasourceName;
    @Value("${spring.datasource.url}")
    private String datasourceUrl;
    @Value("${spring.datasource.username}")
    private String datasourceUsername;
    @Value("${spring.datasource.password}")
    private String datasourcePassword;
    @Value("${spring.flyway.baseline-on-migrate:true}")
    private Boolean flywayBaselineOnMigrate;
    @Value("${spring.flyway.locations}")
    private String flywayLocations;

    @Bean
    @Primary
    public ConnectionFactory connectionFactory() {
        String url = datasourceUrl;
        if (url.startsWith("jdbc:h2:")) {
            url = url.substring(8);
        }
        return new H2ConnectionFactory(H2ConnectionConfiguration.builder()
            .inMemory(datasourceName)
            .url(url)
            .username(datasourceUsername)
            .password(datasourcePassword)
            .build());
    }

    @Bean
    @Primary
    public Flyway flyway() {
        return Flyway.configure()
            .baselineOnMigrate(flywayBaselineOnMigrate)
            .dataSource(datasourceUrl, datasourceUsername, datasourcePassword)
            .locations(flywayLocations)
            .load();
    }

    @Bean
    public FlywayMigrationInitializer flywayMigrationInitializer(Flyway flyway) {
        return new FlywayMigrationInitializer(flyway);
    }

}
