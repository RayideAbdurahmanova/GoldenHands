package com.matrix.Java._Spring.utily;

import org.springframework.http.HttpMethod;

public class EndpointRoles {
    public static final String[] PUBLIC_ENDPOINTS = {
            "/auth/**",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/swagger-ui.html",
            "/webjars/swagger-ui/**"
    };

    public static final EndpointRule[] PRODUCT_RULES = {
            new EndpointRule(HttpMethod.GET, "/products", new String[]{"USER", "ADMIN", "SELLER"}),
            new EndpointRule(HttpMethod.GET, "/products/{id}", new String[]{"USER", "ADMIN", "SELLER"}),
            new EndpointRule(HttpMethod.POST, "/products", new String[]{"SELLER"}),
            new EndpointRule(HttpMethod.PUT, "/products/{id}", new String[]{"SELLER"}),
            new EndpointRule(HttpMethod.DELETE, "/products/{id}", new String[]{"SELLER"}),
            new EndpointRule(HttpMethod.GET, "/products/category/{categoryId}", new String[]{"USER", "ADMIN", "SELLER"}),
            new EndpointRule(HttpMethod.GET, "/products/seller/{sellerId}", new String[]{"USER", "ADMIN", "SELLER"})
    };
    public static final EndpointRule[] BASKET_RULES = {
            new EndpointRule(HttpMethod.POST, "/basket", new String[]{"USER"}),
            new EndpointRule(HttpMethod.DELETE, "/basket/{productId}", new String[]{"USER"}),
            new EndpointRule(HttpMethod.GET, "/basket", new String[]{"USER"}),
    };

    public static final EndpointRule[] ADDRESS_RULES = {
            new EndpointRule(HttpMethod.POST, "/addresses", new String[]{"USER"}),
            new EndpointRule(HttpMethod.GET, "/addresses", new String[]{"ADMIN"}),
            new EndpointRule(HttpMethod.GET, "/addresses/{id}", new String[]{"ADMIN"}),
            new EndpointRule(HttpMethod.GET, "/addresses/my-address", new String[]{"ADMIN", "SELLER", "USER"})
    };

    public static final EndpointRule[] CATEGORY_RULES = {
            new EndpointRule(HttpMethod.GET, "/categories", new String[]{"ADMIN", "SELLER", "USER"}),
            new EndpointRule(HttpMethod.GET, "/categories/{id}", new String[]{"ADMIN", "SELLER", "USER"}),
            new EndpointRule(HttpMethod.POST, "/categories", new String[]{"ADMIN"}),
            new EndpointRule(HttpMethod.PUT, "/categories/{id}", new String[]{"ADMIN"}),
            new EndpointRule(HttpMethod.DELETE, "/categories/{id}", new String[]{"ADMIN"}),
            new EndpointRule(HttpMethod.GET, "/categories/parenId/{parentId}", new String[]{"ADMIN", "SELLER", "USER"}),
    };

    public static final EndpointRule[] CUSTOMER_RULES = {
            new EndpointRule(HttpMethod.GET, "/customers/profile", new String[]{"ADMIN", "USER"}),
            new EndpointRule(HttpMethod.GET, "/customers", new String[]{"ADMIN"}),
            new EndpointRule(HttpMethod.PUT, "/customers/change-password", new String[]{"USER", "ADMIN", "SELLER"})
    };

    public static final EndpointRule[] ORDER_RULES = {
            new EndpointRule(HttpMethod.GET, "/orders/customer", new String[]{"USER"}),
            new EndpointRule(HttpMethod.GET, "/orders/customer-order/{id}", new String[]{"USER"}),
            new EndpointRule(HttpMethod.GET, "/orders/{id}", new String[]{"ADMIN"}),
            new EndpointRule(HttpMethod.GET, "/orders", new String[]{"ADMIN"}),
            new EndpointRule(HttpMethod.POST, "/orders", new String[]{"USER"})
    };

    public static final EndpointRule[] REVIEWS_RULES = {
            new EndpointRule(HttpMethod.GET, "/reviews/product/{productId}", new String[]{"USER", "ADMIN", "SELLER"}),
            new EndpointRule(HttpMethod.GET, "/reviews/user", new String[]{"USER"}),
            new EndpointRule(HttpMethod.POST, "/reviews", new String[]{"USER"}),
            new EndpointRule(HttpMethod.PUT, "/reviews/{id}", new String[]{"USER"}),
            new EndpointRule(HttpMethod.DELETE, "/reviews/{productId}", new String[]{"USER", "SELLER"})
    };

    public static final EndpointRule[] WISHLIST_RULES = {
            new EndpointRule(HttpMethod.GET, "/wishlist/user", new String[]{"USER"}),
            new EndpointRule(HttpMethod.POST, "/wishlist", new String[]{"USER"}),
            new EndpointRule(HttpMethod.DELETE, "/wishlist", new String[]{"USER"}),
            new EndpointRule(HttpMethod.DELETE, "/wishlist/product/{productId}", new String[]{"USER"})
    };

}
