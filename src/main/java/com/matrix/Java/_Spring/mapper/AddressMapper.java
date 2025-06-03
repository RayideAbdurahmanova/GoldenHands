package com.matrix.Java._Spring.mapper;


import com.matrix.Java._Spring.dto.AddressDto;
import com.matrix.Java._Spring.dto.CreateAddressRequest;
import com.matrix.Java._Spring.dto.CreateOrderRequest;
import com.matrix.Java._Spring.model.entity.Address;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel="spring")
public interface AddressMapper {

    List<AddressDto> getAddressDtoList(List<Address> addresses);


    AddressDto toAddressDtoGetById(Address address);

    Address toCreateAddressRequest(CreateAddressRequest createAddressRequest);

    Address updateAddressFromDto(CreateAddressRequest createAddressRequest, @MappingTarget Address address);

    Address toAddress(CreateOrderRequest createOrderRequest);


}
