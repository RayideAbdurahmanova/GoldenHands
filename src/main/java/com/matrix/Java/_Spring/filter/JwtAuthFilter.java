package com.matrix.Java._Spring.filter;

import com.matrix.Java._Spring.jwt.AuthService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final List<AuthService> authServices;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Optional<Authentication> authenticationOptional=Optional.empty();
        for(AuthService authService: authServices){
            authenticationOptional=authenticationOptional.or(()->authService.getAuthentication(request));
        }
        authenticationOptional.ifPresent(auth-> SecurityContextHolder.getContext().setAuthentication(auth));
        filterChain.doFilter(request,response);

    }
}
