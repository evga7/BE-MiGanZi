package com.StreetNo5.StreetNo5;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
@EnableAsync
@EnableAspectJAutoProxy
public class StreetNo5Application {

	public static void main(String[] args) {

		SpringApplication.run(StreetNo5Application.class, args);
	}

}
