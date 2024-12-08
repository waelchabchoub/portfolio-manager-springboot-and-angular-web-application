package com.wael.chabchoub.portfoliomanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.wael.chabchoub.portfoliomanager.security.config.PortfolioManagerProperties;

@SpringBootApplication
@EnableConfigurationProperties(PortfolioManagerProperties.class)
public class PortfolioManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(PortfolioManagerApplication.class, args);
	}

}
