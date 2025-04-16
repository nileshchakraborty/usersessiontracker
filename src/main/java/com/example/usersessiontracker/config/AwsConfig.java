package com.example.usersessiontracker.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.firehose.FirehoseClient;
import software.amazon.awssdk.services.athena.AthenaClient;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.glue.GlueClient;

import java.net.URI;

@Configuration
public class AwsConfig {
    
    @Value("${aws.region}")
    private String region;
    
    private static final String LOCALSTACK_ENDPOINT = "http://localhost:4566";
    private static final String ACCESS_KEY = "test";
    private static final String SECRET_KEY = "test";
    
    @Bean
    public DynamoDbClient dynamoDbClient() {
        return DynamoDbClient.builder()
                .endpointOverride(URI.create(LOCALSTACK_ENDPOINT))
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(ACCESS_KEY, SECRET_KEY)))
                .build();
    }
    
    @Bean
    public FirehoseClient firehoseClient() {
        return FirehoseClient.builder()
                .endpointOverride(URI.create(LOCALSTACK_ENDPOINT))
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(ACCESS_KEY, SECRET_KEY)))
                .build();
    }
    
    @Bean
    public AthenaClient athenaClient() {
        return AthenaClient.builder()
                .endpointOverride(URI.create(LOCALSTACK_ENDPOINT))
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(ACCESS_KEY, SECRET_KEY)))
                .build();
    }
    
    @Bean
    public SqsClient sqsClient() {
        return SqsClient.builder()
                .endpointOverride(URI.create(LOCALSTACK_ENDPOINT))
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(ACCESS_KEY, SECRET_KEY)))
                .build();
    }
    
    @Bean
    public GlueClient glueClient() {
        return GlueClient.builder()
                .endpointOverride(URI.create(LOCALSTACK_ENDPOINT))
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(ACCESS_KEY, SECRET_KEY)))
                .build();
    }
} 