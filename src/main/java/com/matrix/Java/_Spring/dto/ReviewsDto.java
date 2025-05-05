package com.matrix.Java._Spring.dto;


import lombok.Data;

@Data
public class ReviewsDto {

//    @ManyToOne
//    @JoinColumn(name = "product_id")
    private Long productId;

//    @ManyToOne
//    @JoinColumn(name = "user_id", nullable = false)
    private Long customerId;

//    @Column( nullable = false)
    private Integer rating;

    private String comment;
}
