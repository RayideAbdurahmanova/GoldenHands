package com.matrix.Java._Spring.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
public class CreateOrderRequest {
    @NotNull
    private Integer productId;
    @NotNull
    @Min(value = 1, message = "Quantity must be at least 1")
    @Max(value = 10, message = "Quantity cannot exceed 5")
    private Integer quantity;
    private CreateAddressRequest createAddress;
}
