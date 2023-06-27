package com.StreetNo5.StreetNo5;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class StreetNo5Application {

	public static void main(String[] args) {

		SpringApplication.run(StreetNo5Application.class, args);
	}

}
