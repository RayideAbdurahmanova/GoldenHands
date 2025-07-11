package com.matrix.Java._Spring.controller;

import com.matrix.Java._Spring.dto.CustomerSignUp;
import com.matrix.Java._Spring.dto.LoginRequest;
import com.matrix.Java._Spring.dto.ResetPasswordRequest;
import com.matrix.Java._Spring.dto.SellerSignUp;
import com.matrix.Java._Spring.jwt.AuthService;
import com.matrix.Java._Spring.service.AuthResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public AuthResponse login(@RequestBody @Valid LoginRequest loginRequest) {
        return authService.login(loginRequest);
    }

    @PostMapping("verify-account")
    public String verifyAccount(@RequestParam String otp) {
        authService.verifyAccount(otp);
        return "Your account has been verified. Please login app";
    }

    @PostMapping("resend-otp")
    public String regenerateOtp(@RequestParam String email) {
        authService.regenerateOtp(email);
        return "Otp code sent to email. Please verify your account";
    }

    @PostMapping("/customer-signUp")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void customerSignUp(@RequestBody @Valid CustomerSignUp customerSignUp) {
        authService.customerSignUp(customerSignUp);
    }

    @PostMapping("/seller-signUp")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void sellerSignUp(@RequestBody @Valid SellerSignUp sellerSignUp) {
        authService.sellerSignUp(sellerSignUp);

    }

    @PostMapping("forget-password")
    public String forgetPassword(@RequestParam String email) {
        authService.forgetPssword(email);
        return "Otp sent to email. Please reset your password";
    }

    @PostMapping("reset-password")
    public String resetPassword(@RequestParam String otp,
                                @RequestBody @Valid ResetPasswordRequest request) {
        authService.resetPassword(otp, request);
        return "Your password has been reset. Please login app";
    }
}
