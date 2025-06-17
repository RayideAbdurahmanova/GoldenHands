package com.matrix.Java._Spring.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateProductRequest {
    @NotBlank(message = "Product name is required")
    private String productName;
    @NotNull(message = "Price is required")
    private Double price;
    @NotNull(message = "Category ID is required")
    private Long categoryId;
    private String description;
    @NotNull(message = "Quantity in Stock is required")
    private Integer quantityInStock;
}
