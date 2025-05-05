package com.matrix.Java._Spring.service;

import com.matrix.Java._Spring.dto.CreateCustomerRequest;
import com.matrix.Java._Spring.dto.CustomerDto;

import java.util.List;

public interface CustomerService {

    List<CustomerDto> getList();

    CustomerDto getById(Integer id);

    CustomerDto create(CreateCustomerRequest createCustomerRequest);

    CustomerDto update(Integer id, CreateCustomerRequest createCustomerRequest);

    void delete(Integer id);
}
