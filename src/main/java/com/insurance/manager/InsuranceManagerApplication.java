package com.insurance.manager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class InsuranceManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(InsuranceManagerApplication.class, args);
	}

	@org.springframework.context.annotation.Bean
	org.springframework.boot.CommandLineRunner init(com.insurance.manager.service.StorageService storageService) {
		return (args) -> {
			storageService.init();
		};
	}
}
