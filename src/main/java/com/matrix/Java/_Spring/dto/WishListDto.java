package com.matrix.Java._Spring.dto;


import lombok.Data;

@Data
public class WishListDto {


//    @ManyToOne
//    @JoinColumn(name = "user_id")
    private Long customerId;

//    @ManyToOne
//    @JoinColumn(name = "product_id")
    private Long productId;


}
