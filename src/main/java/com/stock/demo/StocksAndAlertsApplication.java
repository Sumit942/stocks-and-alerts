package com.stock.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class StocksAndAlertsApplication {

	public static void main(String[] args) {
		SpringApplication.run(StocksAndAlertsApplication.class, args);
	}

}
