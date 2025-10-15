package com.matrix.Java._Spring.configuration;

import com.matrix.Java._Spring.filter.JwtAuthFilter;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Disable CSRF
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Stateless session
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(auth -> auth
                        // Public endpoints
                        .requestMatchers(
                                "/auth/**",
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/swagger-ui.html",
                                "/webjars/swagger-ui/**"
                        ).permitAll()

                        // USER-only endpoints
                        .requestMatchers(HttpMethod.GET,
                                "/basket",
                                "/orders/customer",
                                "/orders/customer-order/{id}",
                                "/reviews/user",
                                "/wishlist/user",
                                "/addresses/my-address",
                                "/customers/profile"
                                ).hasAuthority("USER")
                        .requestMatchers(HttpMethod.POST,
                                "/basket",
                                "/orders",
                                "/reviews",
                                "/wishlist"
                        ).hasAuthority("USER")
                        .requestMatchers(HttpMethod.PUT,
                                "/reviews/{id}"
                        ).hasAuthority("USER")
                        .requestMatchers(HttpMethod.DELETE,
                                "/basket/{productId}",
                                "/wishlist",
                                "/wishlist/product/{productId}"
                        ).hasAuthority("USER")

                        // ADMIN-only endpoints
                        .requestMatchers(HttpMethod.GET,
                                "/customers",
                                "/orders",
                                "/orders/{id}",
                                "/seller",
                                "/roles"
                        ).hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.POST,
                                "/categories",
                                "/roles",
                                "/roles/assign-role-toUser"
                        ).hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.PUT,
                                "/categories/{id}"
                        ).hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.DELETE,
                                "/categories/{id}",
                                "/roles/remove-role-fromUser"
                        ).hasAuthority("ADMIN")

                        // SELLER-only endpoints
                        .requestMatchers(HttpMethod.POST,
                                "/products"
                        ).hasAuthority("SELLER")
                        .requestMatchers(HttpMethod.PUT,
                                "/products/{id}"
                        ).hasAuthority("SELLER")
                        .requestMatchers(HttpMethod.DELETE,
                                "/products/{id}"
                        ).hasAuthority("SELLER")

                        // USER, ADMIN, SELLER endpoints
                        .requestMatchers(HttpMethod.GET,
                                "/products",
                                "/products/{id}",
                                "/products/category/{categoryId}",
                                "/products/seller/{sellerId}",
                                "/categories",
                                "/categories/{id}",
                                "/categories/parenId/{parentId}",
                                "/reviews/product/{productId}"
                        ).hasAnyAuthority("USER", "ADMIN", "SELLER")

                        // USER, ADMIN endpoints
                        .requestMatchers(HttpMethod.GET,
                                "/addresses",
                                "/addresses/{id}"
                        ).hasAuthority( "ADMIN")

                        // USER, SELLER endpoints
                        .requestMatchers(HttpMethod.DELETE,
                                "/reviews/{productId}"
                        ).hasAnyAuthority("USER", "SELLER")

                        // USER, ADMIN, SELLER endpoints
                        .requestMatchers(HttpMethod.PUT,
                                "/customers/change-password"
                        ).hasAnyAuthority("USER", "ADMIN", "SELLER")

                        // All other requests require authentication
                        .anyRequest().authenticated()
                )
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            response.getWriter().write("Authentication error: Unauthorized access");
                        })
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                            response.getWriter().write("Access denied: Insufficient permissions");
                        })
                );

        return http.build();
    }
}