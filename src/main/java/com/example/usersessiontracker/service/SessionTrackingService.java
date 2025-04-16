package com.example.usersessiontracker.service;

import com.example.usersessiontracker.model.UserSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.firehose.FirehoseClient;
import software.amazon.awssdk.services.firehose.model.PutRecordRequest;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SessionTrackingService {
    
    private final DynamoDbClient dynamoDbClient;
    private final FirehoseClient firehoseClient;
    private final SqsClient sqsClient;
    private final ObjectMapper objectMapper;
    
    @Value("${aws.dynamodb.table}")
    private String dynamoTable;
    
    @Value("${aws.sqs.queue}")
    private String queueUrl;
    
    @Value("${aws.firehose.delivery-stream}")
    private String deliveryStream;
    
    public void trackLogin(String userId, String sessionId, String ipAddress, String userAgent) {
        UserSession session = new UserSession();
        session.setUserId(userId);
        session.setSessionId(sessionId);
        session.setLoginTime(LocalDateTime.now());
        session.setIpAddress(ipAddress);
        session.setUserAgent(userAgent);
        session.setStatus("ACTIVE");
        
        // Store in DynamoDB
        storeInDynamoDB(session);
        
        // Send to SQS for processing
        sendToSQS(session);
    }
    
    public void trackLogout(String sessionId) {
        // Update session in DynamoDB
        UserSession session = updateSessionInDynamoDB(sessionId);
        
        if (session != null) {
            // Calculate session duration
            session.setLogoutTime(LocalDateTime.now());
            session.setSessionDuration(Duration.between(session.getLoginTime(), session.getLogoutTime()).toSeconds());
            session.setStatus("COMPLETED");
            
            // Send to Firehose for S3 storage
            sendToFirehose(session);
        }
    }
    
    private void storeInDynamoDB(UserSession session) {
        // Implementation for storing in DynamoDB
        // This is a simplified version - you'll need to implement the actual DynamoDB operations
    }
    
    private UserSession updateSessionInDynamoDB(String sessionId) {
        // Implementation for updating session in DynamoDB
        // This is a simplified version - you'll need to implement the actual DynamoDB operations
        return null;
    }
    
    private void sendToSQS(UserSession session) {
        try {
            String message = objectMapper.writeValueAsString(session);
            SendMessageRequest request = SendMessageRequest.builder()
                    .queueUrl(queueUrl)
                    .messageBody(message)
                    .build();
            
            sqsClient.sendMessage(request);
        } catch (Exception e) {
            // Handle exception
        }
    }
    
    private void sendToFirehose(UserSession session) {
        try {
            String record = objectMapper.writeValueAsString(session);
            // Implementation for sending to Firehose
            
            firehoseClient.putRecord(PutRecordRequest.builder()
                    .deliveryStreamName(deliveryStream)
                    .record(software.amazon.awssdk.services.firehose.model.Record.builder()
                            .data(SdkBytes.fromString(record, java.nio.charset.StandardCharsets.UTF_8))
                            .build())
                    .build());
            System.out.println("Record sent to Firehose: " + record);

        } catch (Exception e) {
            // Handle exception
        }
    }
} 