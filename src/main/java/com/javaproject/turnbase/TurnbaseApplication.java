package com.javaproject.turnbase;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("com.javaproject.turnbase.repository")
public class TurnbaseApplication {

	public static void main(String[] args) {
		SpringApplication.run(TurnbaseApplication.class, args);
	}

}
