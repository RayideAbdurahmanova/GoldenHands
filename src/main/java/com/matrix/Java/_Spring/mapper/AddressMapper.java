package com.matrix.Java._Spring.mapper;


import com.matrix.Java._Spring.dto.AddressDto;
import com.matrix.Java._Spring.dto.CreateAddressRequest;
import com.matrix.Java._Spring.model.entity.Address;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel="spring")
public interface AddressMapper {

    List<AddressDto> getAddressDtoList(List<Address> addresses);

    AddressDto toAddressDtoGetById(Address address);

    Address toCreateAddressRequest(CreateAddressRequest createAddressRequest);

//    void updateCustomerFromDto(CreateCustomerRequest request,@MappingTarget Customer customer);
}
