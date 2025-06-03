package com.matrix.Java._Spring.dto;

import lombok.Data;


@Data
public class CreateOrderRequest {
    private Integer productId;
    private Integer quantity;
    private String city;
    private String street;
    private String zipCode;
    private String state;
}
