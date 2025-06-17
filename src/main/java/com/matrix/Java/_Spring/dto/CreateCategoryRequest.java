package com.matrix.Java._Spring.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateCategoryRequest {
    @NotNull(message = "Category name is required")
    private String categoryName;
    private Long parentCategoryId;
}
