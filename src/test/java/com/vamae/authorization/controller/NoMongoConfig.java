package com.vamae.authorization.controller;

import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.test.context.TestConfiguration;

@TestConfiguration
@ImportAutoConfiguration(exclude = {
        MongoAutoConfiguration.class
})
public class NoMongoConfig {
}