package com.matrix.Java._Spring.filter;

import com.matrix.Java._Spring.jwt.JwtService;
import com.matrix.Java._Spring.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthFilter extends OncePerRequestFilter {
    private final UserRepository userRepository;
    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        final String token;
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (StringUtils.isEmpty(authHeader) || !StringUtils.startsWith(authHeader, "Bearer ")) {
            log.info("No valid Authorization header found, proceeding with filter chain");
            filterChain.doFilter(request, response);
            return;
        }

        String url = request.getRequestURI();
        log.info("url: {}", url);
        token = authHeader.substring(7).trim();

        try {
            jwtService.validateToken(token);
            var email = jwtService.extractUserName(token);
            if (StringUtils.isNotEmpty(email)) {
                var user = userRepository.findByUsername(email).orElseThrow();
                var authorities = user.getAuthorities();
                authorities.forEach(a -> System.out.println("Authority: " + a.getAuthority()));
                log.info("Setting authentication for user: {}", email);
                SecurityContextHolder.getContext().setAuthentication(
                        new UsernamePasswordAuthenticationToken(user, token, user.getAuthorities())
                );
                log.info("SecurityContext before filterChain: {}",
                        SecurityContextHolder.getContext().getAuthentication() != null
                                ? SecurityContextHolder.getContext().getAuthentication().getAuthorities()
                                : "null");
            } else {
                log.warn("No email extracted from token");
            }
        } catch (Exception e) {
            log.error("Token validation failed: {}", e.getMessage());
            filterChain.doFilter(request, response);
            return;
        }

        filterChain.doFilter(request, response);
        log.info("SecurityContext after filterChain: {}",
                SecurityContextHolder.getContext().getAuthentication() != null
                        ? SecurityContextHolder.getContext().getAuthentication().getAuthorities()
                        : "null");
        log.info("Request method: {}, URL: {}", request.getMethod(), request.getRequestURI());
    }
}