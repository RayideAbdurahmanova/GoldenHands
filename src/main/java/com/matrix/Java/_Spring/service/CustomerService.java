package com.matrix.Java._Spring.service;

import com.matrix.Java._Spring.dto.CustomerDto;
import com.matrix.Java._Spring.dto.UserProfile;

import java.util.List;

public interface CustomerService {
    UserProfile getMyProfile();

    List<CustomerDto> getAll();
}
