package com.matrix.Java._Spring.mapper;


import com.matrix.Java._Spring.dto.OrderProductDto;
import com.matrix.Java._Spring.model.entity.OrderProducts;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel="spring")
public interface OrderProductMapper {

    @Mapping(target = "orderId", source = "order.orderId")
    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "price", source = "price")
    @Mapping(target = "quantity", source = "quantity")
    OrderProductDto toOrderProductDtoGetById(OrderProducts orderProduct);

}
