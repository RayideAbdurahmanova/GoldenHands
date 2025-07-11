package com.matrix.Java._Spring.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class CreateProductRequest {
    @NotBlank(message = "Product name is required")
    private String productName;
    @NotNull(message = "Price is required")
    @Positive(message = "Price must be positive")
    private Double price;
    @NotNull(message = "Category ID is required")
    @Positive(message = "Category ID must be positive")
    private Long categoryId;
    private String description;
    @NotNull(message = "Quantity in Stock is required")
    @Positive(message = "Quantity  must be positive")
    private Integer quantityInStock;
}
