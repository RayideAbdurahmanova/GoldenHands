package com.matrix.Java._Spring.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CustomerSignUp {
    @NotBlank(message = "Email can not be null")
    @Email
    private String email;
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*])[A-Za-z\\d!@#$%^&*]{6,20}$",
            message = "Password must contain at least 1 uppercase letter, 1 lowercase letter, 1 digit, and 1 special character")
    @NotBlank(message = "Password can not be null")
    @Size(min = 6, max = 20, message = "Password must contain at last 6 characters")
    private String password;
    @NotBlank(message = "Confirm password can not be null")
    private String confirmPassword;
    @NotBlank(message = "Phone number can not be null")
    @Pattern(regexp = "^\\+994[0-9]{9}$",
            message = "Phone number must start with +994 and be followed by exactly 9 digits")
    private String phoneNumber;
}
