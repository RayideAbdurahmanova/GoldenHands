package com.matrix.Java._Spring.filter;


import com.matrix.Java._Spring.jwt.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.List;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class JwtAuthFilterConfigurerAdapter  extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    private final List<AuthService> authServices;

    @Override
    public void configure(HttpSecurity httpSecurity){
        log.trace("Added auth request filter");
        httpSecurity.addFilterBefore(new JwtAuthFilter(authServices), UsernamePasswordAuthenticationFilter.class);
    }
}
