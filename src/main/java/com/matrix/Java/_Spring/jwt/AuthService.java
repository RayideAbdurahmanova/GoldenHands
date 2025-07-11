package com.matrix.Java._Spring.jwt;

import com.matrix.Java._Spring.dto.CustomerSignUp;
import com.matrix.Java._Spring.dto.LoginRequest;
import com.matrix.Java._Spring.dto.ResetPasswordRequest;
import com.matrix.Java._Spring.dto.SellerSignUp;
import com.matrix.Java._Spring.service.AuthResponse;

public interface AuthService {

    AuthResponse login(LoginRequest loginRequest);

    void customerSignUp(CustomerSignUp customerSignUp);

    void sellerSignUp(SellerSignUp sellerSignUp);

    void verifyAccount(String otp);

    void regenerateOtp(String email);

    void forgetPssword(String email);

    void resetPassword(String otp, ResetPasswordRequest request);
}
