package com.example.usersessiontracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class UserSessionTrackerApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserSessionTrackerApplication.class, args);
    }
} 