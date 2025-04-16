package com.example.usersessiontracker.service;

import com.example.usersessiontracker.model.UserSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.firehose.FirehoseClient;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import software.amazon.awssdk.services.sqs.model.SendMessageResponse;
import software.amazon.awssdk.services.firehose.model.PutRecordBatchRequest;
import software.amazon.awssdk.services.firehose.model.PutRecordBatchResponse;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class SessionTrackingServiceTest {

    @Mock
    private DynamoDbClient dynamoDbClient;

    @Mock
    private FirehoseClient firehoseClient;

    @Mock
    private SqsClient sqsClient;

    @InjectMocks
    private SessionTrackingService sessionTrackingService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testTrackLogin() {
        // Given
        String userId = "testUser";
        String sessionId = "testSessionId";
        String ipAddress = "127.0.0.1";
        String userAgent = "testUserAgent";

        when(sqsClient.sendMessage(any(SendMessageRequest.class)))
            .thenReturn(SendMessageResponse.builder().build());

        // When
        sessionTrackingService.trackLogin(userId, sessionId, ipAddress, userAgent);

        // Then
        verify(sqsClient).sendMessage(any(SendMessageRequest.class));
        // Add more verifications for DynamoDB operations once implemented
    }

    @Test
    public void testTrackLogout() {
        // Given
        String sessionId = "testSessionId";
        UserSession mockSession = new UserSession();
        mockSession.setSessionId(sessionId);
        mockSession.setLoginTime(java.time.LocalDateTime.now());

        when(firehoseClient.putRecordBatch(any(PutRecordBatchRequest.class)))
            .thenReturn(PutRecordBatchResponse.builder().build());

        // When
        sessionTrackingService.trackLogout(sessionId);

        // Then
        verify(firehoseClient, times(1)).putRecordBatch(any(PutRecordBatchRequest.class));
        // Add more verifications for DynamoDB operations once implemented
    }
} 