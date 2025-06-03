package com.matrix.Java._Spring.service;

import com.matrix.Java._Spring.dto.*;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;


public interface OrderProductService {

    List<OrderProductDto> getWithOrderId(Integer orderId, HttpServletRequest request);

    OrderProductDto getById(Integer id,HttpServletRequest request);

    OrderProductDto create(CreateOrderProductRequest createOrderProductRequest, HttpServletRequest request);

    OrderProductDto update(Integer id, CreateOrderProductRequest createOrderProductRequest, HttpServletRequest request);

    void delete(Integer orderId,Integer orderProductId,HttpServletRequest request);

}
