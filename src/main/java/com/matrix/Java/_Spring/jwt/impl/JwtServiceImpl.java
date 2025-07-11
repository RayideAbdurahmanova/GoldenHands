package com.matrix.Java._Spring.jwt.impl;

import com.matrix.Java._Spring.jwt.JwtService;
import com.matrix.Java._Spring.model.entity.Role;

import com.matrix.Java._Spring.model.entity.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;

@Service
@Slf4j
public class JwtServiceImpl implements JwtService {
    @Value("${secret.key}")
    private String secret;

    @PostConstruct
    public Key secretKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    @Override
    public String issueToken(User user) {
        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("userId", user.getId())
                .claim("roles", user.getRoles()
                        .stream().map(Role::getName).toList())
                .setIssuedAt(new Date())
                .setHeaderParam("typ", "JWT")
                .setExpiration(Date.from(Instant.now().plus(Duration.ofDays(1))))
                .signWith(secretKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractUserName(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    @Override
    public void validateToken(String token) {
        expired(token);
    }

    @Override
    public Integer extractUserId(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("userId", Integer.class);
    }

    @Override
    public void expired(String token) {
        Jwts.parserBuilder()
                .setSigningKey(secretKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
    }
}
