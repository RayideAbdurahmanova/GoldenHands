package com.matrix.Java._Spring.service;

import com.matrix.Java._Spring.dto.CreateOrderRequest;
import com.matrix.Java._Spring.dto.OrderDto;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface OrderService {

    List<OrderDto> getListByCustomerId();

    OrderDto getById(Integer id);

    OrderDto create(CreateOrderRequest createOrderRequest, HttpServletRequest request);

    OrderDto update(Integer id, CreateOrderRequest createOrderRequest, HttpServletRequest request);


    OrderDto getCustomerOrder(Integer id);

    List<OrderDto> getAll();
}
