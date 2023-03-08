/*
package com.example.reactor.ProjectReactor.config;

import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "com.example.reactor.ProjectReactor.repository")
public class MongoDBConfig extends AbstractReactiveMongoConfiguration {
    @Value("{mongodb.database.name}")
    private String databaseName;
    @Value("{mongodb.database.host}")
    private String databaseHost;

    @Override
    protected String getDatabaseName() {
        return databaseName;
    }

    @Override
    public MongoClient reactiveMongoClient() {
        String name = databaseHost;
        return MongoClients.create(name);
    }

    @Bean
    public ReactiveMongoTemplate reactiveMongoTemplate(){
        return new ReactiveMongoTemplate(reactiveMongoClient(),getDatabaseName());
    }

}
*/
