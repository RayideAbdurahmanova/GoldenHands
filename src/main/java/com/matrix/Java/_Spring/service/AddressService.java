package com.matrix.Java._Spring.service;

import com.matrix.Java._Spring.dto.AddressDto;
import com.matrix.Java._Spring.dto.CreateAddressRequest;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface AddressService {

    List<AddressDto> getList();

    AddressDto getById(Integer id);

    AddressDto getByCustomerId();

    void create(CreateAddressRequest createAddressRequest, HttpServletRequest request);

    void delete(HttpServletRequest request);
}
