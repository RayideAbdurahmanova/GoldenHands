package com.matrix.Java._Spring.jwt;

import com.matrix.Java._Spring.model.entity.security.User;
import io.jsonwebtoken.Claims;

public interface JwtService {

    String issueToken(User user);

    Claims verifyToken(String Token);


}
