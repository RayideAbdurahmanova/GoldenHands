package com.matrix.Java._Spring.configuration;

import com.matrix.Java._Spring.filter.JwtAuthFilterConfigurerAdapter;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig  {

    private final JwtAuthFilterConfigurerAdapter configurerAdapter;


    @Bean
    public static BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        List<String> adminEndpoints = List.of(
                "/categories",
                "/categories/*",
                "/products",
                "/products/*",
                "/orders",
                "/orders/*",
                "/payments",
                "/payments/*",
                "/order_products",
                "/order_products/*",
                "/customers",
                "/customers/*",
                "/wishlist",
                "/wishlist/*",
                "/reviews",
                "/reviews/*",
                "/addresses",
                "/addresses/*"
                );
        final JwtAuthFilterConfigurerAdapter apply = http.apply(configurerAdapter);
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers(adminEndpoints.toArray(new String[0])).hasAnyAuthority("ADMIN")
                                .anyRequest()
                                .permitAll()
                ).exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint((request, response, authException) ->
                                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED) //login olmadan daxil olmaq
                        )
                        .accessDeniedHandler((request, response, accessDeniedException) ->
                                response.setStatus(HttpServletResponse.SC_FORBIDDEN) //user kimi daxil olub admin hissesine daxil olur
                        )
                ).httpBasic(Customizer.withDefaults());

        return http.build();
    }


}
