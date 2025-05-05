package com.matrix.Java._Spring.service;

import com.matrix.Java._Spring.dto.*;


public interface OrderProductService {

    OrderProductDto getById(Integer id);

    OrderProductDto create(CreateOrderProductRequest createOrderProductRequest);

    OrderProductDto update(Integer id, CreateOrderProductRequest createOrderProductRequest);

    void delete(Integer orderId,Integer orderProductId);

//    List<OrderProductDto> getListWithOrderId(Integer orderId);
}
