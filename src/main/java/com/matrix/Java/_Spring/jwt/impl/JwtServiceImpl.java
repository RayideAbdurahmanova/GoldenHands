package com.matrix.Java._Spring.jwt.impl;

import com.matrix.Java._Spring.jwt.JwtService;
import com.matrix.Java._Spring.model.entity.security.Authority;
import com.matrix.Java._Spring.model.entity.security.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
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
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class JwtServiceImpl implements JwtService {


    @Value("${issue.key}")
    private String issueKey;

    @PostConstruct
    public Key secretKey() {
        byte[] keyBytes = Decoders.BASE64.decode(issueKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    @Override
    public String issueToken(User user) {
        List<String> authorities = user
                .getAuthorities()
                .stream()
                .map(Authority::getAuthority)
                .toList();

        final JwtBuilder jwtBuilder = Jwts.builder()
                .setSubject(user.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(Date.from(Instant.now().plus(Duration.ofDays(1))))
                .addClaims(Map.of("roles", authorities,
                        "userId",user.getId(),
                        "username",user.getUsername() // userId varsa, bu iş asanlaşır – DB-yə əlavə müraciət etmədən istifadəçinin ID-sini bilirsən
                ))
                .setHeader(Map.of("type", "JWT"))
                .signWith(secretKey(), SignatureAlgorithm.HS256);

        return jwtBuilder.compact();
    }

    @Override
    public Claims verifyToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
