package com.example.usersessiontracker.controller;

import com.example.usersessiontracker.service.SessionTrackingService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/api/sessions")
public class SessionController {
    
    private final SessionTrackingService sessionTrackingService;
    
    public SessionController(SessionTrackingService sessionTrackingService) {
        this.sessionTrackingService = sessionTrackingService;
    }
    
    @PostMapping("/login")
    public String login(@RequestParam String userId, HttpServletRequest request) {
        String sessionId = UUID.randomUUID().toString();
        String ipAddress = request.getRemoteAddr();
        String userAgent = request.getHeader("User-Agent");
        
        sessionTrackingService.trackLogin(userId, sessionId, ipAddress, userAgent);
        return sessionId;
    }
    
    @PostMapping("/logout")
    public void logout(@RequestParam String sessionId) {
        sessionTrackingService.trackLogout(sessionId);
    }
} 