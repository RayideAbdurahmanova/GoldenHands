package com.matrix.Java._Spring.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateWishListRequest {

    @NotNull(message = "Product ID is required")
    private Long productId;
}
