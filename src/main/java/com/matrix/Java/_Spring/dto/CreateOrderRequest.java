package com.matrix.Java._Spring.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
public class CreateOrderRequest {

    @NotNull
    private CreateAddressRequest createAddress;
}
