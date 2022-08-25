package com.senacor.intermission.newjava;

import com.senacor.intermission.newjava.config.HikariRuntimeHints;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportRuntimeHints;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableJpaRepositories
@EnableJpaAuditing
@EnableScheduling
@ImportRuntimeHints(HikariRuntimeHints.class)
public class IntermissionNewJavaApplication {

	public static void main(String[] args) {
		SpringApplication.run(IntermissionNewJavaApplication.class, args);
	}

}
