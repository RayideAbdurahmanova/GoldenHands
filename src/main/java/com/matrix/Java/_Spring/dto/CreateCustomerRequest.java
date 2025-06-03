package com.matrix.Java._Spring.dto;

import lombok.Data;


@Data
public class    CreateCustomerRequest {

    private String name;
    private String username;
    private String email;
    private String password;
}
