package com.example.usersessiontracker.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private long jwtExpirationInMs;

    public String generateToken(Authentication authentication) {
        String username = authentication.getName();
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        return JWT.create()
                .withSubject(username)
                .withClaim("roles", authorities)
                .withExpiresAt(new Date(System.currentTimeMillis() + jwtExpirationInMs))
                .sign(Algorithm.HMAC256(jwtSecret));
    }

    public DecodedJWT validateToken(String token) {
        return JWT.require(Algorithm.HMAC256(jwtSecret))
                .build()
                .verify(token);
    }
} 