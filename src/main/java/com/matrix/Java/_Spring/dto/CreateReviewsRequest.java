package com.matrix.Java._Spring.dto;


import lombok.Data;

@Data
public class CreateReviewsRequest {

    private Integer rating;
    private String comment;
    private Integer productId;
}
