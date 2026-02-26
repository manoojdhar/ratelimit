package com.ratelimiter.ratelimitter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.")
public class RatelimitterApplication {

	public static void main(String[] args) {
		SpringApplication.run(RatelimitterApplication.class, args);
	}

}
