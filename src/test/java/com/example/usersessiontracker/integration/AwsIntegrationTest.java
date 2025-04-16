package com.example.usersessiontracker.integration;

import com.example.usersessiontracker.config.AwsConfig;
import com.example.usersessiontracker.model.UserSession;
import com.example.usersessiontracker.service.SessionTrackingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.GetQueueUrlRequest;
import software.amazon.awssdk.services.sqs.model.GetQueueUrlResponse;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ActiveProfiles("test")
public class AwsIntegrationTest {

    @Autowired
    private DynamoDbClient dynamoDbClient;

    @Autowired
    private SqsClient sqsClient;

    @Autowired
    private SessionTrackingService sessionTrackingService;

    @Test
    public void testAwsClientsInitialization() {
        assertNotNull(dynamoDbClient);
        assertNotNull(sqsClient);
    }

    @Test
    public void testSqsQueueAccess() {
        // This test assumes the queue exists in the test environment
        GetQueueUrlRequest request = GetQueueUrlRequest.builder()
                .queueName("user-session-queue")
                .build();

        GetQueueUrlResponse response = sqsClient.getQueueUrl(request);
        assertNotNull(response.queueUrl());
    }

    @Test
    public void testSessionTrackingFlow() {
        // Given
        String userId = "testUser-" + UUID.randomUUID();
        String sessionId = UUID.randomUUID().toString();
        String ipAddress = "127.0.0.1";
        String userAgent = "testUserAgent";

        // When
        sessionTrackingService.trackLogin(userId, sessionId, ipAddress, userAgent);

        // Then
        // Verify the session was created in DynamoDB
        // This would require actual DynamoDB access in the test environment
        // For now, we just verify no exceptions were thrown
    }
} 