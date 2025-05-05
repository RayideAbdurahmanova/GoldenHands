package com.matrix.Java._Spring.dto;


import lombok.Data;

@Data
public class OrderProductDto {

//    @ManyToOne
//    @JoinColumn(name = "order_id",nullable = false)
    private Long orderId;

//    @ManyToOne
//    @JoinColumn(name = "product_id")
    private Long productId;

//    @Column(nullable = false)
    private Integer quantity;

//    @Column(nullable = false)
    private double price;


}
