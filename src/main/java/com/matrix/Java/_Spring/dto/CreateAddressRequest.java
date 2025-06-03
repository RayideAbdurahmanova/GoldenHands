package com.matrix.Java._Spring.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateAddressRequest {
    private String city;
    private String street;
    @NotBlank
    private String zipCode;
}
