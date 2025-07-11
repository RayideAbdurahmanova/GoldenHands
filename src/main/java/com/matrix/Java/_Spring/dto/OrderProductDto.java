package com.matrix.Java._Spring.dto;


import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OrderProductDto {
    @NotNull
    private Long orderId;
    @NotNull
    private Long productId;
    @NotNull
    private Integer quantity;
    @NotNull
    private double price;
}
