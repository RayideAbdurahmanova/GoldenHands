package com.matrix.Java._Spring.mapper;

import com.matrix.Java._Spring.dto.*;
import com.matrix.Java._Spring.model.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel="spring",uses= AddressMapper.class)
public interface OrderMapper {

    List<OrderDto> toOrderDtoList(List<Order> orders);

    @Mapping(target="orderProducts",  ignore = true)
    OrderDto toOrderDtoGetById(Order order);

    @Mapping(target="orderProducts",  ignore = true)
    Order toOrderAdd(CreateOrderRequest createOrderRequest);

    @Mapping(target="orderId", ignore=true)
    void updateOrderProductFromDto(CreateOrderRequest createOrderRequest, @MappingTarget Order order);
}
