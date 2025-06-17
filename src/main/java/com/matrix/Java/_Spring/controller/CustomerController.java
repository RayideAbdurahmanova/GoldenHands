package com.matrix.Java._Spring.controller;

import com.matrix.Java._Spring.dto.ChangePasswordRequest;
import com.matrix.Java._Spring.dto.CustomerDto;
import com.matrix.Java._Spring.dto.UserProfile;
import com.matrix.Java._Spring.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping("/profile")
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public UserProfile getMyProfile(){
        return customerService.getMyProfile();
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<CustomerDto> getUsers(){
        return customerService.getAll();
    }

    @PutMapping("change-password")
    @PreAuthorize("hasAnyAuthority('USER','ADMIN','SELLER')")
    public void changePassword(@RequestBody @Valid ChangePasswordRequest changePasswordRequest){
        customerService.changePassword(changePasswordRequest);
    }
}
