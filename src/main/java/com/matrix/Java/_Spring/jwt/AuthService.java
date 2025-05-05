package com.matrix.Java._Spring.jwt;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;

import java.util.Optional;

public interface AuthService {

    Optional<Authentication> getAuthentication(HttpServletRequest request);

}
