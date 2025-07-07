package com.solux.piccountbe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class PiccountBeApplication {
	public static void main(String[] args) {
		SpringApplication.run(PiccountBeApplication.class, args);
	}

}
