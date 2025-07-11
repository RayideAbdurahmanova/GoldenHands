package com.matrix.Java._Spring.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class BasketRequest {
    @NotNull
    @Positive(message = "Product ID must be positive")
    private Integer productId;
    @NotNull
    @Positive(message = "Quantity must ve positive")
    private Integer quantity;
}
