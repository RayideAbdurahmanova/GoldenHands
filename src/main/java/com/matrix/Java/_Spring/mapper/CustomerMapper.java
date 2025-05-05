package com.matrix.Java._Spring.mapper;

import com.matrix.Java._Spring.dto.CreateCustomerRequest;
import com.matrix.Java._Spring.dto.CustomerDto;
import com.matrix.Java._Spring.model.entity.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel="spring")
public interface CustomerMapper {

    List<CustomerDto> getCustomerDtoList(List<Customer> customers);

    CustomerDto toCustomerDtoGetById(Customer customer);

    Customer toCreateCustomerRequest(CreateCustomerRequest createCustomerRequest);

    @Mapping(target = "customerId", ignore = true)
    void updateCustomerFromDto(CreateCustomerRequest request,@MappingTarget Customer customer);
}
