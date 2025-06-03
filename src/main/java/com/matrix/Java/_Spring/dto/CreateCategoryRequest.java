package com.matrix.Java._Spring.dto;

import lombok.Data;

@Data
public class CreateCategoryRequest {
    private String categoryName;
    private Long parentCategoryId;
}
