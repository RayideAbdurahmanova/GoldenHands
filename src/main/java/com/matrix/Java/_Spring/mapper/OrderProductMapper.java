package com.matrix.Java._Spring.mapper;


import com.matrix.Java._Spring.dto.CreateOrderProductRequest;
import com.matrix.Java._Spring.dto.OrderProductDto;
import com.matrix.Java._Spring.model.entity.OrderProducts;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel="spring")
public interface OrderProductMapper {

    List<OrderProductDto> getOrderProductDtoList(List<OrderProducts> orderProducts);

    OrderProductDto toOrderProductDtoGetById(OrderProducts orderProduct);

    OrderProducts toCreateOrderProductRequest(CreateOrderProductRequest createOrderProductRequest);

    @Mapping(target="orderProductId", ignore=true)
    void updateOrderProductFromDto(CreateOrderProductRequest createOrderProductRequest,@MappingTarget OrderProducts existing);
}
