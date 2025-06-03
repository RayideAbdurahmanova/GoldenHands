package com.matrix.Java._Spring.jwt.impl;

import com.matrix.Java._Spring.dto.*;
import com.matrix.Java._Spring.exceptions.DataExistException;
import com.matrix.Java._Spring.exceptions.DataNotFoundException;
import com.matrix.Java._Spring.jwt.AuthService;
import com.matrix.Java._Spring.jwt.JwtService;
import com.matrix.Java._Spring.model.entity.Customer;
import com.matrix.Java._Spring.model.entity.Role;
import com.matrix.Java._Spring.model.entity.Seller;
import com.matrix.Java._Spring.model.entity.User;
import com.matrix.Java._Spring.repository.CustomerRepository;
import com.matrix.Java._Spring.repository.RoleRepository;
import com.matrix.Java._Spring.repository.UserRepository;
import com.matrix.Java._Spring.service.AuthResponse;
import com.matrix.Java._Spring.service.impl.EmailServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final CustomerRepository customerRepository;
    private final EmailServiceImpl emailService;

    @Override
    public AuthResponse login(LoginRequest loginRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        var user = userRepository.findByUsername(loginRequest.getEmail()).orElseThrow();

        var accessToken = jwtService.issueToken(user);
        return AuthResponse.builder()
                .accessToken(accessToken)
                .build();
    }


    @Override
    @Transactional
    public void customerSignUp(CustomerSignUp customerSignUp) {
        if (userRepository.existsByUsername(customerSignUp.getEmail())) {
            throw new DataExistException("This email already exists");
        }
        if (!customerSignUp.getPassword().equals(customerSignUp.getConfirmPassword())) {
            throw new DataNotFoundException("Passwords do not match");
        }
        if (userRepository.existsByPhoneNumber(customerSignUp.getPhoneNumber())) {
            throw new DataExistException("This phone number already exists");
        }

        Customer customer = new Customer();
        User user = new User();
        user.setUsername(customerSignUp.getEmail());
        user.setPassword(passwordEncoder.encode(customerSignUp.getPassword()));
        user.setPhoneNumber(customerSignUp.getPhoneNumber());
        user.setEnabled(false);
        user.setCustomer(customer);
        user.setRoles(createRole());
        String otp = createOtp();
        user.setOtp(otp);
        user.setCreateOtpTime(LocalDateTime.now());
        userRepository.save(user);
        emailService.verifyAccount(user.getUsername(), otp);
    }

    @Override
    public void sellerSignUp(SellerSignUp sellerSignUp) { //add
        if (userRepository.existsByUsername(sellerSignUp.getEmail())) {
            throw new DataExistException("This email already exists");
        }
        if (!sellerSignUp.getPassword().equals(sellerSignUp.getConfirmPassword())) {
            throw new DataNotFoundException("Passwords do not match");
        }
        if (userRepository.existsByPhoneNumber(sellerSignUp.getPhoneNumber())) {
            throw new DataExistException("This phone number already exists");
        }

        Seller seller = new Seller();
        seller.setBusinessAddress(sellerSignUp.getBusinessAddress());
        seller.setStoreName(sellerSignUp.getStoreName());
        User user = new User();
        user.setUsername(sellerSignUp.getEmail());
        user.setPassword(passwordEncoder.encode(sellerSignUp.getPassword()));
        user.setPhoneNumber(sellerSignUp.getPhoneNumber());
        user.setEnabled(false);
        user.setRoles(createRole());
        user.setSeller(seller);
        String otp = createOtp();
        user.setOtp(otp);
        user.setCreateOtpTime(LocalDateTime.now());
        userRepository.save(user);
        emailService.verifyAccount(user.getUsername(), otp);
    }

    @Override
    public void verifyAccount(String otp) {
        var user = userRepository.findByOtp(otp).
                orElseThrow(() -> new DataNotFoundException("Otp is not found"));
        if (Duration.between(user.getCreateOtpTime(), LocalDateTime.now()).getSeconds() > 60) {
            throw new DataNotFoundException("Otp is expired, please try regenerate otp");
        }
        user.setOtp(null);
        user.setEnabled(true);
        user.setCreateOtpTime(null);
        userRepository.save(user);
    }

    @Override
    public void regenerateOtp(String email) {
        var user = userRepository.findByUsername(email)
                .orElseThrow(() -> new DataNotFoundException("Email is not found"));
        String otp = createOtp();
        user.setOtp(otp);
        user.setCreateOtpTime(LocalDateTime.now());
        emailService.verifyAccount(user.getUsername(), otp);
        userRepository.save(user);
    }

    @Override
    public void forgetPssword(String email) {
        userRepository.findByUsername(email)
                .ifPresentOrElse(user ->
                        regenerateOtp(email), () -> {
                    throw new DataNotFoundException("User is not found");
                });
    }

    @Override
    public void resetPassword(String otp, ResetPasswordRequest request) {
        var user = userRepository.findByOtp(otp).
                orElseThrow(() -> new DataNotFoundException("Otp is not found or incorrect"));

        if (Duration.between(user.getCreateOtpTime(), LocalDateTime.now()).getSeconds() > 60) {
            throw new DataNotFoundException("Otp is expired, please try regenerate otp");
        }
        if (!request.password().equals(request.confirmPassword())) {
            throw new DataNotFoundException("Passwords do not match");
        }
        user.setPassword(passwordEncoder.encode(request.confirmPassword()));
        user.setOtp(null);
        user.setCreateOtpTime(null);
        userRepository.save(user);
    }

    @Override
    public void changePassword(ChangePasswordRequest changePasswordRequest) {
        var email = SecurityContextHolder.getContext().getAuthentication().getName();
        var user = userRepository.findByUsername(email)
                .orElseThrow(() -> new DataNotFoundException("User is not found"));
        if (!changePasswordRequest.confirmPassword().equals(changePasswordRequest.password())) {
            throw new DataNotFoundException("Passwords do not match");
        }
        if (!passwordEncoder.matches(changePasswordRequest.currentPassword(), user.getPassword())) {
            throw new DataNotFoundException("Current password do not match");
        }
        user.setPassword(passwordEncoder.encode(changePasswordRequest.confirmPassword()));
        userRepository.save(user);
    }


    private Set<Role> createRole() {
        var roleEntity = roleRepository.findByName("USER")
                .orElseThrow(() -> new DataNotFoundException("User Role not found"));
        Set<Role> roles = new HashSet<>();
        roles.add(roleEntity);
        return roles;
    }

    public String createOtp() {
        Random random = new Random();
        return String.valueOf(1000 + random.nextInt(9999));
    }
}
