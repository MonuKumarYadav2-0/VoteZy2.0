package com.backend.votezy20;

import java.util.concurrent.Executor;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableScheduling
@EnableAsync
@EnableCaching
@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);

	}

	@Bean(name = "emailExecutor")
	public Executor emailExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(2);
		executor.setMaxPoolSize(5);
		executor.setQueueCapacity(50);
		executor.setThreadNamePrefix("EmailExecutor-");
		executor.initialize();
		return executor;

	}

	@Bean
	CommandLineRunner runner(PasswordEncoder passwordEncoder) {

		return args -> {

			System.out.println(passwordEncoder.encode("12345678"));
		};
	}

}
