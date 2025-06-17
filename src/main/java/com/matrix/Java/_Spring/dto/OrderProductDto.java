package com.matrix.Java._Spring.dto;


import lombok.Data;

import java.util.List;

@Data
public class OrderProductDto {
    private Long orderId;
    private Long productId;
    private Integer quantity;
    private double price;
    private List<ProductDto> products;
    private List<OrderDto> orders;
}
