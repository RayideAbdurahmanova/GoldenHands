package com.matrix.Java._Spring.dto;

import com.matrix.Java._Spring.enums.UserRole;
import lombok.Data;


@Data
public class CreateCustomerRequest {

    private String name;

    private String username;

    private String email;

    private String password;

    private UserRole role;


}
