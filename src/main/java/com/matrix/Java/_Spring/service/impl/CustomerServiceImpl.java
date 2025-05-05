package com.matrix.Java._Spring.service.impl;

import com.matrix.Java._Spring.dto.CategoryDto;
import com.matrix.Java._Spring.dto.CreateCustomerRequest;
import com.matrix.Java._Spring.dto.CustomerDto;
import com.matrix.Java._Spring.exceptions.DataNotFoundException;
import com.matrix.Java._Spring.mapper.CustomerMapper;
import com.matrix.Java._Spring.model.entity.Customer;
import com.matrix.Java._Spring.repository.CustomerRepository;
import com.matrix.Java._Spring.service.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @Override
    public List<CustomerDto> getList() {
        return customerMapper.getCustomerDtoList(customerRepository.findAll());
    }

    @Override
    public CustomerDto getById(Integer id) {
        return customerMapper.toCustomerDtoGetById(customerRepository.findById(id).orElseThrow(
                ()->new DataNotFoundException("Data Not Found With ID:"+id+" CustomerID")
        ));
    }

    @Override
    public CustomerDto create(CreateCustomerRequest createCustomerRequest) {
        Customer customer=customerMapper.toCreateCustomerRequest(createCustomerRequest);
        Customer savedCustomer=customerRepository.save(customer);
        return customerMapper.toCustomerDtoGetById(savedCustomer);
    }




    @Override
    public CustomerDto update(Integer id, CreateCustomerRequest createCustomerRequest) {
        Customer customer=customerRepository.findById(id).orElseThrow(
                ()->new RuntimeException("Customer with ID " + id + " not found"));

        customerMapper.updateCustomerFromDto(createCustomerRequest,customer);

        Customer updatedCustomer = customerRepository.save(customer);
        return customerMapper.toCustomerDtoGetById(updatedCustomer);



    }

    @Override
    public void delete(Integer id) {

        Customer customer=customerRepository.findById(id).orElseThrow(
                ()->new RuntimeException("Customer with ID " + id + " not found"));

        customerRepository.deleteById(id);
    }
}
