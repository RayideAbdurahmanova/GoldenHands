package com.matrix.Java._Spring.jwt;

import com.matrix.Java._Spring.model.entity.User;

public interface JwtService {

    String issueToken(User user);

    void expired(String Token);

    String extractUserName(String token);

    void validateToken(String token);

    Integer extractUserId(String token);
}
