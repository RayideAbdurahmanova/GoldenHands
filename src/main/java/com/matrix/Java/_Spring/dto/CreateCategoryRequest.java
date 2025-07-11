package com.matrix.Java._Spring.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class CreateCategoryRequest {
    @NotNull(message = "Category name is required")
    private String categoryName;
    @Positive(message = "Category ID must be positive")
    private Long parentCategoryId;
}
