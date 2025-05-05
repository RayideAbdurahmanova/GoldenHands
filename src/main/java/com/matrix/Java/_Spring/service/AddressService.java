package com.matrix.Java._Spring.service;

import com.matrix.Java._Spring.dto.AddressDto;
import com.matrix.Java._Spring.dto.CategoryDto;
import com.matrix.Java._Spring.dto.CreateAddressRequest;
import com.matrix.Java._Spring.dto.CreateCategoryRequest;
import com.matrix.Java._Spring.mapper.AddressMapper;

import java.util.List;

public interface AddressService {

    List<AddressDto> getList();

    AddressDto getById(Integer id);

    AddressDto getByCustomerId(Integer customerId);

    AddressDto create(CreateAddressRequest createAddressRequest);

    AddressDto update(Integer id, CreateAddressRequest createAddressRequest);

    void delete(Integer id);
}
