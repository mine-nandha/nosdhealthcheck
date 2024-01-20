package com.mine.nosd.healthcheck.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "com.mine.nosd.healthcheck")
public class MongoConfig extends AbstractMongoClientConfiguration {
    @Value("${MONGO_DB_URL}")
    String dbUrl;
    @Value("${MONGO_DB_NAME}")
    String dbName;

    @Override
    protected String getDatabaseName() {
        return dbName;
    }

    @Override
    public MongoClient mongoClient() {
        return MongoClients.create(dbUrl);
    }
}
