package com.matrix.Java._Spring.dto;


import lombok.Data;

@Data
public class ReviewsDto {

    private Long productId;
    private Long customerId;
    private Integer rating;
    private String comment;
}
