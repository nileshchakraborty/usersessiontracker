package com.example.usersessiontracker.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.utility.DockerImageName;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.firehose.FirehoseClient;
import software.amazon.awssdk.services.athena.AthenaClient;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.glue.GlueClient;

@Configuration
public class LocalStackConfig {

    private static final DockerImageName LOCALSTACK_IMAGE = DockerImageName.parse("localstack/localstack:1.4.0");
    private static final LocalStackContainer localStack = new LocalStackContainer(LOCALSTACK_IMAGE)
            .withServices(
                    LocalStackContainer.Service.DYNAMODB,
                    LocalStackContainer.Service.S3,
                    LocalStackContainer.Service.SQS,
                    LocalStackContainer.Service.KINESIS,
                    LocalStackContainer.Service.CLOUDWATCH
            );

    static {
        localStack.start();
    }

    @Bean
    @Primary
    public DynamoDbClient dynamoDbClient() {
        return DynamoDbClient.builder()
                .endpointOverride(localStack.getEndpointOverride(LocalStackContainer.Service.DYNAMODB))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(localStack.getAccessKey(), localStack.getSecretKey())))
                .region(Region.of(localStack.getRegion()))
                .build();
    }

    @Bean
    @Primary
    public FirehoseClient firehoseClient() {
        return FirehoseClient.builder()
                .endpointOverride(localStack.getEndpointOverride(LocalStackContainer.Service.KINESIS))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(localStack.getAccessKey(), localStack.getSecretKey())))
                .region(Region.of(localStack.getRegion()))
                .build();
    }

    @Bean
    @Primary
    public AthenaClient athenaClient() {
        return AthenaClient.builder()
                .endpointOverride(localStack.getEndpointOverride(LocalStackContainer.Service.CLOUDWATCH))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(localStack.getAccessKey(), localStack.getSecretKey())))
                .region(Region.of(localStack.getRegion()))
                .build();
    }

    @Bean
    @Primary
    public SqsClient sqsClient() {
        return SqsClient.builder()
                .endpointOverride(localStack.getEndpointOverride(LocalStackContainer.Service.SQS))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(localStack.getAccessKey(), localStack.getSecretKey())))
                .region(Region.of(localStack.getRegion()))
                .build();
    }

    @Bean
    @Primary
    public GlueClient glueClient() {
        return GlueClient.builder()
                .endpointOverride(localStack.getEndpointOverride(LocalStackContainer.Service.CLOUDWATCH))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(localStack.getAccessKey(), localStack.getSecretKey())))
                .region(Region.of(localStack.getRegion()))
                .build();
    }
} 