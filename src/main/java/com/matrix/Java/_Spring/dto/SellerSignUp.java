package com.matrix.Java._Spring.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SellerSignUp {
    @NotBlank(message = "Email can not be null")
    private String email;
    @NotBlank(message = "Password can not be null")
    private String password;
    @NotBlank(message = "Confirm password can not be null")
    private String confirmPassword;
    @NotBlank(message = "Phone number can not be null")
    private String phoneNumber;
    private String businessAddress;
    private String storeName;
}
