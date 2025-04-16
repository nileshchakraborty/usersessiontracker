package com.example.usersessiontracker.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.firehose.FirehoseClient;
import software.amazon.awssdk.services.athena.AthenaClient;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.glue.GlueClient;

@TestConfiguration
public class TestConfig {
    
    @Bean
    public DynamoDbClient dynamoDbClient() {
        return DynamoDbClient.builder().build();
    }
    
    @Bean
    public FirehoseClient firehoseClient() {
        return FirehoseClient.builder().build();
    }
    
    @Bean
    public AthenaClient athenaClient() {
        return AthenaClient.builder().build();
    }
    
    @Bean
    public SqsClient sqsClient() {
        return SqsClient.builder().build();
    }
    
    @Bean
    public GlueClient glueClient() {
        return GlueClient.builder().build();
    }
} 