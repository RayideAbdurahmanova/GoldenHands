package com.matrix.Java._Spring.dto;


import lombok.Data;

@Data
public class CreateOrderProductRequest {

//    private Long orderId; //ozu gelecek

    private Long productId;

    private Integer quantity;

 //   private double price; productId ile gelecek
}
