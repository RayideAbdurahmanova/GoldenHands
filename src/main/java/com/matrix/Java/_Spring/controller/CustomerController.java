package com.matrix.Java._Spring.controller;

import com.matrix.Java._Spring.dto.CustomerDto;
import com.matrix.Java._Spring.dto.UserProfile;
import com.matrix.Java._Spring.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
