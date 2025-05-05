package com.matrix.Java._Spring.service.impl;

import com.matrix.Java._Spring.dto.CreateOrderProductRequest;
import com.matrix.Java._Spring.dto.OrderProductDto;
import com.matrix.Java._Spring.exceptions.DataNotFoundException;
import com.matrix.Java._Spring.mapper.OrderProductMapper;

import com.matrix.Java._Spring.model.entity.Order;
import com.matrix.Java._Spring.model.entity.OrderProducts;
import com.matrix.Java._Spring.repository.OrderProductRepository;
import com.matrix.Java._Spring.repository.OrderRepository;
import com.matrix.Java._Spring.service.OrderProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;



@Service
@Slf4j
@RequiredArgsConstructor
public class OrderProductServiceImpl implements OrderProductService {

    private final OrderProductMapper orderProductMapper;
    private final OrderProductRepository orderProductRepository;

//
//    @Override
//    public List<OrderProductDto> getWithOrderId(Order order) {
//       if(!order.getCustomer().getCustomerId().equals(customerId))
//        return orderProductMapper.getOrderProductDtoList(orderProductRepository.findByOrder(order));
//    }

    @Override
    public OrderProductDto getById(Integer id) {
        OrderProducts orderProduct = orderProductRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("OrderProduct with ID " + id + " not found"));
        return orderProductMapper.toOrderProductDtoGetById(orderProduct);
    }

    @Override
    public OrderProductDto create(CreateOrderProductRequest createOrderProductRequest) {
        OrderProducts orderProducts = orderProductMapper.toCreateOrderProductRequest(createOrderProductRequest);
        OrderProducts saved = orderProductRepository.save(orderProducts);
        return orderProductMapper.toOrderProductDtoGetById(saved);
    }

    @Override
    public OrderProductDto update(Integer id, CreateOrderProductRequest createOrderProductRequest) {
        OrderProducts existing = orderProductRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("OrderProduct with ID " + id + " not found"));

        orderProductMapper.updateOrderProductFromDto(createOrderProductRequest, existing);

        OrderProducts updated = orderProductRepository.save(existing);
        return orderProductMapper.toOrderProductDtoGetById(updated);
    }

    @Override
    public void delete(Integer orderId,Integer orderProductId) {

        OrderProducts orderProducts = orderProductRepository.findById(orderProductId)
                .orElseThrow(() -> new RuntimeException("OrderProduct with ID " + orderProductId + " not found"));

        if(orderProducts.getOrder().getOrderId().equals(orderId)){
            orderProductRepository.delete(orderProducts);
        }else {
            throw new DataNotFoundException("OrderProduct does not belong to the provided Order");

        }
        orderProductRepository.delete(orderProducts);



    }
}
