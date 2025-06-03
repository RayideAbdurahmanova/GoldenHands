package com.matrix.Java._Spring.dto;


import jakarta.validation.constraints.NotBlank;

public record ResetPasswordRequest(
        @NotBlank
        String password,
        @NotBlank
        String confirmPassword) {
}
