package com.example.usersessiontracker.model;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.*;

public class UserSessionTest {

    @Test
    public void testUserSessionCreation() {
        // Given
        String userId = "testUser";
        String sessionId = "testSessionId";
        LocalDateTime loginTime = LocalDateTime.now();
        String ipAddress = "127.0.0.1";
        String userAgent = "testUserAgent";

        // When
        UserSession session = new UserSession();
        session.setUserId(userId);
        session.setSessionId(sessionId);
        session.setLoginTime(loginTime);
        session.setIpAddress(ipAddress);
        session.setUserAgent(userAgent);
        session.setStatus("ACTIVE");

        // Then
        assertNotNull(session);
        assertEquals(userId, session.getUserId());
        assertEquals(sessionId, session.getSessionId());
        assertEquals(loginTime, session.getLoginTime());
        assertEquals(ipAddress, session.getIpAddress());
        assertEquals(userAgent, session.getUserAgent());
        assertEquals("ACTIVE", session.getStatus());
        assertNull(session.getLogoutTime());
        assertNull(session.getSessionDuration());
    }

    @Test
    public void testSessionDurationCalculation() {
        // Given
        UserSession session = new UserSession();
        LocalDateTime loginTime = LocalDateTime.now();
        LocalDateTime logoutTime = loginTime.plus(30, ChronoUnit.MINUTES);
        long expectedDuration = 30 * 60; // 30 minutes in seconds

        // When
        session.setLoginTime(loginTime);
        session.setLogoutTime(logoutTime);
        session.setSessionDuration(expectedDuration);

        // Then
        assertEquals(expectedDuration, session.getSessionDuration());
        assertEquals(loginTime, session.getLoginTime());
        assertEquals(logoutTime, session.getLogoutTime());
    }
} 