package com.matrix.Java._Spring.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateAddressRequest {
    @NotNull(message = "City is required")
    private String city;
    @NotNull(message = "Street is required")
    private String street;
    @NotBlank(message = "ZipCode is required")
    private String zipCode;
}
