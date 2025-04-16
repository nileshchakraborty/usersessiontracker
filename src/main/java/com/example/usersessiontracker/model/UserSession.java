package com.example.usersessiontracker.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.Duration;

@Entity
@Table(name = "user_sessions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSession {
    @Id
    private String sessionId;
    private String userId;
    private LocalDateTime loginTime;
    private LocalDateTime logoutTime;
    private String ipAddress;
    private String userAgent;
    private String status;
    
    @Transient
    private Long sessionDuration;

    public Long getSessionDuration() {
        if (loginTime != null && logoutTime != null) {
            return Duration.between(loginTime, logoutTime).getSeconds();
        }
        return null;
    }

    public void setSessionDuration(Long duration) {
        this.sessionDuration = duration;
    }
} 