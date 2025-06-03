package com.matrix.Java._Spring.dto;


import lombok.Data;

@Data
public class OrderProductDto {
    private Long orderId;
    private Long productId;
    private Integer quantity;
    private double price;
}
