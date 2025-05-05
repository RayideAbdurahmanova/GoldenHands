package com.matrix.Java._Spring.service;

import com.matrix.Java._Spring.dto.CreateOrderRequest;
import com.matrix.Java._Spring.dto.OrderDto;

import java.util.List;

public interface OrderService {

    List<OrderDto> getListByCustomerId(Integer customerId);

    OrderDto getById(Integer id);

    OrderDto create(CreateOrderRequest createOrderRequest);

    OrderDto update(Integer id, CreateOrderRequest createOrderRequest);

    void delete(Integer id);
}
