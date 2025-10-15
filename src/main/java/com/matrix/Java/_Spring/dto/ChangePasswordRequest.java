package com.matrix.Java._Spring.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ChangePasswordRequest(
        @NotBlank
        @Size(min = 6, max = 20, message = "Size is incorrect")
        @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*])[A-Za-z\\d!@#$%^&*]{6,20}$",
                message = "Password must contain at least 1 uppercase letter, 1 lowercase letter, 1 digit, and 1 special character")
        String password,
        @NotBlank
        String confirmPassword,
        @NotBlank
        String currentPassword
) {
}
