package com.matrix.Java._Spring.dto;


import lombok.Data;

import java.util.List;

@Data
public class ProductDto {

    private String productName;
    private double price;
    private Long categoryId;
    private Long sellerId;
    private String description;
    private Integer quantityInStock;
    private List<ReviewsDto> reviews;
}
