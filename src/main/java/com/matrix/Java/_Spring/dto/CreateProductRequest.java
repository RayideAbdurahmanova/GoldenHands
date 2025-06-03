package com.matrix.Java._Spring.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateProductRequest {
    @NotBlank
    private String productName;
    @NotBlank
    private double price;
    @NotBlank
    private Long categoryId;
    private String description;
    @NotBlank
    private Integer quantityInStock;
}
