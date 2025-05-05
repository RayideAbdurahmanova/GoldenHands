package com.matrix.Java._Spring.dto;

import lombok.Data;

import java.util.List;

@Data
public class CreateOrderRequest {

    private List<CreateOrderProductRequest> products; // Product ID + Quantity
    private String shippingStreet;
    private String shippingCity;
    private String shippingState;
    private String shippingZipCode;
    private String shippingCountry;
}
