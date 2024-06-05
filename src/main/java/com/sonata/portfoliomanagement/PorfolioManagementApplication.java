package com.sonata.portfoliomanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
//@ComponentScan(basePackages = "com.sonata.portfoliomanagement")
public class PorfolioManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(PorfolioManagementApplication.class, args);
	}

}
