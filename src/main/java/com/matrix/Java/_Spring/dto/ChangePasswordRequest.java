package com.matrix.Java._Spring.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ChangePasswordRequest(
        @NotBlank
        @Size(min = 6, max = 20, message = "Size is incorrect")
        String password,
        @NotBlank
        String confirmPassword,
        @NotBlank
        String currentPassword
) {
}
