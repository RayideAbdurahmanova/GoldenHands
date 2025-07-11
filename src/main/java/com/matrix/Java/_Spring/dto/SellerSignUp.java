package com.matrix.Java._Spring.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SellerSignUp {
    @NotBlank(message = "Email can not be null")
    private String email;
    @NotBlank(message = "Password can not be null")
    @Size(min = 6, max = 20, message = "Size is incorrect")
    private String password;
    @NotBlank(message = "Confirm password can not be null")
    private String confirmPassword;
    @NotBlank(message = "Phone number can not be null")
    private String phoneNumber;
    private String businessAddress;
    @NotBlank(message = "Store name can not be null")
    private String storeName;
}
