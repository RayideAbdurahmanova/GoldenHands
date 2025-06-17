package com.matrix.Java._Spring.service.impl;

import com.matrix.Java._Spring.dto.ChangePasswordRequest;
import com.matrix.Java._Spring.dto.CustomerDto;
import com.matrix.Java._Spring.dto.UserProfile;
import com.matrix.Java._Spring.exceptions.DataNotFoundException;
import com.matrix.Java._Spring.mapper.CustomerMapper;
import com.matrix.Java._Spring.model.entity.User;
import com.matrix.Java._Spring.repository.UserRepository;
import com.matrix.Java._Spring.service.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerServiceImpl implements CustomerService {

    private final UserRepository userRepository;
    private final CustomerMapper customerMapper;
    private final PasswordEncoder passwordEncoder;



    @Override
    public UserProfile getMyProfile() {
        log.info("Start retrieval of user profile");
        var email = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("email: {}", email);
        var userEntity = userRepository.findByUsername(email)
                .orElseThrow(() -> new UsernameNotFoundException("Data not found"));
        log.info("userEntity: {}", userEntity);
        UserProfile userProfile=customerMapper.mapToUserProfile(userEntity);
        log.info("Finished retrieval {} user profile successfully", userProfile);
        return userProfile;
    }

    @Override
    public List<CustomerDto> getAll() {
        log.info("Start retrieval of all customers");
        List<User> userEntities = userRepository.findAll();
        List<CustomerDto> customerDtoList = customerMapper.mapToDtoList(userEntities);

        log.info("Retrieved {} users with phone numbers", customerDtoList.size());
        log.info("Retrieved {} customers", customerDtoList.size());
        log.info("Finished retrieval {} customers", customerDtoList.size());
        return customerDtoList;
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
}