package com.matrix.Java._Spring.service.impl;

import com.matrix.Java._Spring.dto.CreateOrderProductRequest;
import com.matrix.Java._Spring.dto.OrderProductDto;
import com.matrix.Java._Spring.exceptions.AccessDeniedException;
import com.matrix.Java._Spring.exceptions.DataNotFoundException;
import com.matrix.Java._Spring.jwt.JwtService;
import com.matrix.Java._Spring.mapper.OrderMapper;
import com.matrix.Java._Spring.mapper.OrderProductMapper;

import com.matrix.Java._Spring.mapper.ProductMapper;
import com.matrix.Java._Spring.model.entity.Order;
import com.matrix.Java._Spring.model.entity.OrderProducts;
import com.matrix.Java._Spring.model.entity.Product;
import com.matrix.Java._Spring.repository.OrderProductRepository;
import com.matrix.Java._Spring.repository.OrderRepository;
import com.matrix.Java._Spring.repository.ProductRepository;
import com.matrix.Java._Spring.service.OrderProductService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;


@Service
@Slf4j
@RequiredArgsConstructor
public class OrderProductServiceImpl implements OrderProductService {

    private final OrderProductMapper orderProductMapper;
    private final OrderProductRepository orderProductRepository;
    private final OrderRepository orderRepository;
    private final JwtService jwtService;
    private final ProductRepository productRepository;
    private final OrderMapper orderMapper;
    private final ProductMapper productMapper;


    @Override
    public List<OrderProductDto> getWithOrderId(Integer orderId, HttpServletRequest request) {
        log.info("Starting retrieval of order products with orderId: {}", orderId);
        var token = request.getHeader("Authorization").substring(7).trim();
        var userId = jwtService.extractUserId(token);
        Order order = orderRepository.findById(orderId).
                orElseThrow(() -> new DataNotFoundException("Order not found"));
        if (!order.getCustomer().getUser().getId().equals(userId)) {
            throw new AccessDeniedException("You do not have permission to access this resource");
        }
        List<OrderProducts> orderProducts = orderProductRepository.findByOrder(order);
        List<OrderProductDto> orderProductDtoes = orderProductMapper.getOrderProductDtoList(orderProducts);
        log.info("Finished retrieval {} order products for order ID {}", orderProducts.size(), orderId);
        var orderList = orderProducts.stream()
                .map(OrderProducts::getOrder).toList();

        var prdouctList = orderProducts.stream()
                .map(OrderProducts::getProduct).toList();
        var listOrderDto = orderMapper.toOrderDtoList(orderList);
        var productDt = productMapper.getProductDtoList(prdouctList);

        orderProductDtoes.forEach(orderProductDto -> orderProductDto.setOrders(listOrderDto));
        orderProductDtoes.forEach(orderProductDto -> orderProductDto.setProducts(productDt));
        return orderProductDtoes;
    }


    @Override
    public OrderProductDto getById(Integer id, HttpServletRequest request) {
        log.info("Starting retrieval of order product with id: {}", id);
        var token = request.getHeader("Authorization").substring(7).trim();
        var userId = jwtService.extractUserId(token);
        OrderProducts orderProducts = orderProductRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Order not found"));
        if (!orderProducts.getOrder().getCustomer().getUser().getId().equals(userId)) {
            throw new AccessDeniedException("You do not have permission to access this resource");
        }
        OrderProductDto orderProductDto = orderProductMapper.toOrderProductDtoGetById(orderProducts);
        log.info("Finished retrieval {} order product successfully", orderProductDto);
        return orderProductDto;
    }

    @Override
    @Transactional
    public OrderProductDto create(CreateOrderProductRequest createOrderProductRequest, HttpServletRequest request) {
        log.info("Starting creation of order products: {}", createOrderProductRequest);
        var token = request.getHeader("Authorization").substring(7).trim();
        var userId = jwtService.extractUserId(token);
        Order order = orderRepository.findById(createOrderProductRequest.getOrderId())
                .orElseThrow(() -> new DataNotFoundException("Order not found"));
        Product product = productRepository.findById(createOrderProductRequest.getProductId())
                .orElseThrow(() -> new DataNotFoundException("Product not found"));
        if (!order.getCustomer().getCustomerId().equals(userId)) {
            throw new AccessDeniedException("You do not have permission to access this resource");
        }
        OrderProducts orderProducts = orderProductMapper.toCreateOrderProductRequest(createOrderProductRequest);
        orderProducts.setOrder(order);
        orderProducts.setProduct(product);
        orderProducts.setPrice(BigDecimal.valueOf(product.getPrice()));
        orderProductRepository.save(orderProducts);
        OrderProducts saved = orderProductRepository.save(orderProducts);
        OrderProductDto orderProductDto = orderProductMapper.toOrderProductDtoGetById(saved);
        log.info("Finished creation of order product with id: {}", saved.getOrderProductId());
        return orderProductDto;
    }

    @Override
    @Transactional
    public OrderProductDto update(Integer id, CreateOrderProductRequest createOrderProductRequest, HttpServletRequest request) {
        log.info("Starting update of order products: {}", createOrderProductRequest);
        var token = request.getHeader("Authorization").substring(7).trim();
        var userId = jwtService.extractUserId(token);
        OrderProducts existing = orderProductRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("OrderProduct with ID " + id + " not found"));

        if (!existing.getOrder().getCustomer().getCustomerId().equals(userId)) {
            throw new AccessDeniedException("You do not have permission to access this resource");
        }

        orderProductMapper.updateOrderProductFromDto(createOrderProductRequest, existing);
        OrderProducts updated = orderProductRepository.save(existing);
        OrderProductDto orderProductDto = orderProductMapper.toOrderProductDtoGetById(updated);
        log.info("Updated order product with ID {}", id);
        log.info("Finished update of order product with ID {} successfully", updated.getOrderProductId());
        return orderProductDto;
    }

    @Override
    @Transactional
    public void delete(Integer orderId, Integer orderProductId, HttpServletRequest request) {
        log.info("Starting  deletion of order product ID: {}", orderProductId);
        var token = request.getHeader("Authorization").substring(7).trim();
        var userId = jwtService.extractUserId(token);
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new DataNotFoundException("Order not found"));
        if (!order.getCustomer().getCustomerId().equals(userId)) {
            throw new AccessDeniedException("Order does not belong to customer with ID " + userId);
        }
        OrderProducts orderProducts = orderProductRepository.findById(orderProductId)
                .orElseThrow(() -> new RuntimeException("OrderProduct with ID " + orderProductId + " not found"));
        if (!orderProducts.getOrder().getOrderId().equals(orderId)) {
            throw new AccessDeniedException("You do not have permission to access this resource");
        }
        orderProductRepository.delete(orderProducts);
        log.info("Deleted order product with ID {} for order ID {}", orderProductId, orderId);
        log.info("Finished deletion of order product with ID {} successfully", orderProductId);
    }
}
