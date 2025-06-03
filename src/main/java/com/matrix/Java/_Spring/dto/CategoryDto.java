package com.matrix.Java._Spring.dto;


import lombok.Data;


@Data
public class CategoryDto {

    private Long id;
    private String categoryName;
    private Long parentCategoryId;

}
