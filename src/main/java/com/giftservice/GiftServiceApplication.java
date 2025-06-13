package com.giftservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class GiftServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(GiftServiceApplication.class, args);
	}

}