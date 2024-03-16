package com.vamae.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
public class MongoDBConfiguration {


    @Bean
    public MongoClient mongoClient() {
        return MongoClients.create(Dotenv.load().get("MONGODB_URI"));
    }
    @Bean
    public MongoOperations mongoTemplate() {
        return new MongoTemplate(mongoClient(), "test");
    }
}
