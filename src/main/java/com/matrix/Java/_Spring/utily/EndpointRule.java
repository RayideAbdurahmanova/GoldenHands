package com.matrix.Java._Spring.utily;

import org.springframework.http.HttpMethod;

public record EndpointRule(HttpMethod method, String pattern, String[] roles) {
}
