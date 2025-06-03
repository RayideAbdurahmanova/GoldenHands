package com.matrix.Java._Spring.dto;


import lombok.Data;

@Data
public class CreateOrderProductRequest {
    private Integer orderId;
    private Integer productId;
    private Integer quantity;
}
