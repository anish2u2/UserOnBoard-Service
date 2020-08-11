package com.onboard.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
public class UserOnBoardServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserOnBoardServiceApplication.class, args);
	}

}
