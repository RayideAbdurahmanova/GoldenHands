package com.matrix.Java._Spring.dto;


import lombok.Data;

@Data
public class CreateAddressRequest {

    private String street;
    private String city;
    private String state;
    private String zipCode;
    private String country;
}
