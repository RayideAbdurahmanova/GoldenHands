package com.matrix.Java._Spring.dto;

import lombok.Data;

@Data
public class CreateProductRequest {

    private String productName;

    private double price;

    private Long categoryId;

    private String description;

    private Integer quantityInStock;


}
