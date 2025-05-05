package com.matrix.Java._Spring.service.impl;

import com.matrix.Java._Spring.dto.AddressDto;
import com.matrix.Java._Spring.dto.CreateAddressRequest;
import com.matrix.Java._Spring.exceptions.DataNotFoundException;
import com.matrix.Java._Spring.mapper.AddressMapper;
import com.matrix.Java._Spring.mapper.AddressMapperImpl;
import com.matrix.Java._Spring.model.entity.Address;
import com.matrix.Java._Spring.model.entity.Customer;
import com.matrix.Java._Spring.repository.AddressRepository;
import com.matrix.Java._Spring.repository.CustomerRepository;
import com.matrix.Java._Spring.service.AddressService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;
    private final CustomerRepository customerRepository;


    @Override
    public List<AddressDto> getList() {
        List<Address> addresses=addressRepository.findAll();
        return addressMapper.getAddressDtoList(addresses);
    }

    @Override
    public AddressDto getById(Integer id) {
        Address address=addressRepository.findById(id)
                .orElseThrow(()->new DataNotFoundException("Address not found"));

        return addressMapper.toAddressDtoGetById(address);
    }

    @Override
    public AddressDto getByCustomerId(Integer customerId) {
        Customer customer=customerRepository.findById(customerId)
                .orElseThrow(()->new DataNotFoundException("Address not found"));

        Address address=addressRepository.findByCustomer(customer);
        return addressMapper.toAddressDtoGetById(address);
    }

    @Override
    public AddressDto create(CreateAddressRequest createAddressRequest) {
        return null;
    }

    @Override
    public AddressDto update(Integer id, CreateAddressRequest createAddressRequest) {
        return null;
    }

    @Override
    public void delete(Integer id) {

    }
}
