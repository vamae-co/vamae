package com.vamae;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.Properties;

@SpringBootApplication
@EnableMongoRepositories
public class VamaeApplication {

	public static void main(String[] args) {
		SpringApplication application = new SpringApplication(VamaeApplication.class);

		Properties properties = new Properties();
		properties.put("spring.data.mongodb.uri", System.getenv("MONGODB_URI"));
		application.setDefaultProperties(properties);

		application.run(args);
	}

}
