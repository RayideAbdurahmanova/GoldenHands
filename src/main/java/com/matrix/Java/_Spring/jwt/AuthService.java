package com.matrix.Java._Spring.jwt;

import com.matrix.Java._Spring.dto.*;
import com.matrix.Java._Spring.service.AuthResponse;
import jakarta.validation.Valid;

public interface AuthService {

    AuthResponse login(LoginRequest loginRequest);

    void customerSignUp(CustomerSignUp customerSignUp);

    void sellerSignUp(SellerSignUp sellerSignUp);

    void verifyAccount(String otp);

    void regenerateOtp(String email);

    void forgetPssword(String email);

    void resetPassword(String otp, ResetPasswordRequest request);

    void changePassword(ChangePasswordRequest changePasswordRequest);
}
