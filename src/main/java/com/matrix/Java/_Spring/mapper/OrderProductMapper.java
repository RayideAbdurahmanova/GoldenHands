package com.matrix.Java._Spring.mapper;


import com.matrix.Java._Spring.dto.CreateOrderProductRequest;
import com.matrix.Java._Spring.dto.CreateOrderRequest;
import com.matrix.Java._Spring.dto.OrderProductDto;
import com.matrix.Java._Spring.model.entity.Order;
import com.matrix.Java._Spring.model.entity.OrderProducts;
import com.matrix.Java._Spring.model.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel="spring")
public interface OrderProductMapper {

    @Mapping(target = "orderId", source = "order.orderId")
    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "price", source = "price")
    @Mapping(target = "quantity", source = "quantity")
    List<OrderProductDto> getOrderProductDtoList(List<OrderProducts> orderProducts);

    @Mapping(target = "orderId", source = "order.orderId")
    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "price", source = "price")
    @Mapping(target = "quantity", source = "quantity")
    OrderProductDto toOrderProductDtoGetById(OrderProducts orderProduct);

    OrderProducts toCreateOrderProductRequest(CreateOrderProductRequest createOrderProductRequest);

    @Mapping(target="orderProductId", ignore=true)
    void updateOrderProductFromDto(CreateOrderProductRequest createOrderProductRequest,@MappingTarget OrderProducts existing);

    @Mapping(target = "price", source = "product.price")
    @Mapping(target = "quantity", source = "createOrderProductRequest.quantity")
    OrderProducts toOrderProducts(CreateOrderRequest createOrderProductRequest, Product product, Order order);
}
