package com.matrix.Java._Spring.dto;

import jakarta.validation.constraints.NotBlank;

public record ChangePasswordRequest(
        @NotBlank
        String password,
        @NotBlank
        String confirmPassword,
        @NotBlank
        String currentPassword
) {
}
