package com.example.usersessiontracker.controller;

import com.example.usersessiontracker.service.SessionTrackingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SessionController.class)
public class SessionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SessionTrackingService sessionTrackingService;

    @Test
    public void testLogin() throws Exception {
        String userId = "testUser";
        
        mockMvc.perform(post("/api/sessions/login")
                .param("userId", userId))
                .andExpect(status().isOk());

        verify(sessionTrackingService).trackLogin(any(), any(), any(), any());
    }

    @Test
    public void testLogout() throws Exception {
        String sessionId = "testSessionId";
        
        mockMvc.perform(post("/api/sessions/logout")
                .param("sessionId", sessionId))
                .andExpect(status().isOk());

        verify(sessionTrackingService).trackLogout(sessionId);
    }
} 