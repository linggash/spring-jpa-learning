package com.linggash.spring_data_jpa_learning;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class SpringDataJpaLearningApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringDataJpaLearningApplication.class, args);
	}

}
