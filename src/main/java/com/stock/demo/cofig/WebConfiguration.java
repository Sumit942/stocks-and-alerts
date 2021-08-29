package com.stock.demo.cofig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//@Configuration
public class WebConfiguration implements WebMvcConfigurer {

	@Bean
	public WebClient webClient () {
		return WebClient.create();
	}
}
