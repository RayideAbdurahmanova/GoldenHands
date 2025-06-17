package com.matrix.Java._Spring.dto;


import lombok.Data;

@Data
public class ReviewsDto {
    private Integer rating;
    private String comment;
    private Integer productId;
}
