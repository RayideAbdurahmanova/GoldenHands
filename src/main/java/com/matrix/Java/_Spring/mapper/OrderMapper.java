package com.matrix.Java._Spring.mapper;

import com.matrix.Java._Spring.dto.*;
import com.matrix.Java._Spring.model.entity.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring", uses = AddressMapper.class)
public interface OrderMapper {

    @Mapping(target = "customerId", source = "customer.customerId")
    @Mapping(target = "orderProducts", source = "orderProducts")
    List<OrderDto> toOrderDtoList(List<Order> orders);

    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "orderId", source = "order.orderId")
    OrderProductDto toOrderProductDto(OrderProducts orderProduct);


    @Mapping(target = "customerId", source = "customer.customerId")
    @Mapping(target = "shippingAddress", source = "address")
    OrderDto toOrderDtoGetById(Order order);


    @Mapping(target = "orderId", ignore = true)
    void updateOrderProductFromDto(CreateOrderRequest createOrderRequest, @MappingTarget Order order);

    @Mapping(target = "customer", source = "basket.customer")
//@Mapping(target = "totalAmount", source = "basket.totalPrice")
    Order toOrder(CreateOrderRequest createOrderRequest, User user, Basket basket);
}
