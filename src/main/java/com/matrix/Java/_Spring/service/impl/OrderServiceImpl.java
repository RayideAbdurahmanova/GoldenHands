package com.matrix.Java._Spring.service.impl;

import com.matrix.Java._Spring.dto.CreateOrderRequest;
import com.matrix.Java._Spring.dto.OrderDto;
import com.matrix.Java._Spring.enums.OrderStatus;
import com.matrix.Java._Spring.exceptions.DataNotFoundException;
import com.matrix.Java._Spring.jwt.JwtService;
import com.matrix.Java._Spring.mapper.OrderMapper;
import com.matrix.Java._Spring.mapper.OrderProductMapper;
import com.matrix.Java._Spring.model.entity.*;
import com.matrix.Java._Spring.repository.*;
import com.matrix.Java._Spring.service.AddressService;
import com.matrix.Java._Spring.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

import static com.matrix.Java._Spring.enums.OrderStatus.PENDING;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final OrderProductRepository orderProductRepository;
    private final OrderProductMapper orderProductMapper;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final AddressService addressService;


    @Override
    public List<OrderDto> getListByCustomerId() {
        var email= SecurityContextHolder.getContext().getAuthentication().getName();
        var user=userRepository.findByUsername(email)
                        .orElseThrow();
        log.info("Starting retrieval of order list with ID: {}", user.getCustomer().getCustomerId());
        Customer customer = customerRepository.findById(user.getCustomer().getCustomerId())
                .orElseThrow(() -> new DataNotFoundException("Data Not Found With ID:" + user.getCustomer().getCustomerId() + " OrderID"));

        List<Order> orders = orderRepository.findAllByCustomer(customer);
        List<OrderDto> orderDtos = orderMapper.toOrderDtoList(orders);
        log.info("Finished retrieval {} orders with ID: {}", orderDtos.size(), user.getCustomer().getCustomerId());
        return orderDtos;
    }

    @Override
    public OrderDto getById(Integer id) {
        log.info("Starting retrieval of order with ID: {}", id);
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Order with ID " + id + " not found"));
        OrderDto orderDto = orderMapper.toOrderDtoGetById(order);
        log.info("Finished retrieval {} order  successfully", orderDto);
        return orderDto;
    }


    @Override
    @Transactional
    public OrderDto create(CreateOrderRequest createOrderRequest, HttpServletRequest request) {
        log.info("Starting creation of order : {}", createOrderRequest);
        var token = request.getHeader("Authorization").substring(7).trim();
        var userId = jwtService.extractUserId(token);
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException("User with ID " + userId + " not found"));
        var product = productRepository.findById(createOrderRequest.getProductId())
                .orElseThrow(() -> new DataNotFoundException("Product with ID " + createOrderRequest.getProductId() + " not found"));
        var seller = product.getSeller();

        addressService.create(createOrderRequest.getCreateAddress(), request);
        Order order = createNewOrder(createOrderRequest, user, seller);
        BigDecimal totalAmount = calculateTotalAmount(createOrderRequest, order);

        order.setTotalAmount(totalAmount);
        order.setOrderStatus(PENDING);
        Order savedOrder = orderRepository.save(order);
        var orderProducts = saveOrderProducts(createOrderRequest, product, savedOrder);
        log.info("savedDto:{}", order.getOrderProducts());
        log.info("Created order with ID {} for customer ID {}", savedOrder.getOrderId(), userId);
        OrderDto orderDto = orderMapper.toOrderDtoGetById(savedOrder);
        log.info("Finished retrieval {} orderProducts  successfully", orderDto.getOrderProducts());
        var orderProductDto = orderProductMapper.toOrderProductDtoGetById(orderProducts);

        log.info("Finished creation of order  with id: {}", savedOrder.getOrderId());
        orderDto.setOrderProducts(List.of(orderProductDto));
        return orderDto;
    }

    private Order createNewOrder(CreateOrderRequest createOrderRequest, User user, Seller seller) {
        log.info("Starting creation of  new order : {}", createOrderRequest);
        Order order = orderMapper.toOrder(createOrderRequest, user, seller);
        order.setCustomer(user.getCustomer());
        log.info("Created new order with ID: {} successfully", order.getOrderId());
        log.info("Finished creation of new order with id: {}", order.getOrderId());
        return order;
    }


    private BigDecimal calculateTotalAmount(CreateOrderRequest productRequests, Order order) {
        log.info("Starting calculating total amount : {}", order.getTotalAmount());
        BigDecimal totalAmount = BigDecimal.ZERO;
        Product product = productRepository.findById(Math.toIntExact(productRequests.getProductId()))
                .orElseThrow(() -> new DataNotFoundException("Product not found with ID: " + productRequests.getProductId()));
        BigDecimal price = BigDecimal.valueOf(product.getPrice());
        int quantity = productRequests.getQuantity();
        if (quantity > product.getQuantityInStock()) {
            throw new DataNotFoundException("Quantity Exceeds Stock");
        }
        totalAmount = totalAmount.add(price.multiply(BigDecimal.valueOf(quantity)));
        product.setQuantityInStock(product.getQuantityInStock() - quantity);
        productRepository.save(product);
        log.info("Finished calculation total amount {} for order with product ID {}", totalAmount, productRequests.getProductId());
        return totalAmount;
    }


    public OrderProducts saveOrderProducts(CreateOrderRequest request, Product product, Order savedOrder) {
        log.info("Starting saving of order products : {}", request);
        Product productEntity = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new DataNotFoundException("Product not found with ID: " + product.getId()));
        OrderProducts orderProducts = orderProductMapper.toOrderProducts(request, product, savedOrder);
        orderProducts.setQuantity(request.getQuantity());
        orderProducts.setProduct(productEntity);
        orderProducts.setPrice(BigDecimal.valueOf(productEntity.getPrice()));
        productRepository.save(productEntity);
        orderProductRepository.save(orderProducts);
        log.info("Saved order product for order ID {} and product ID {}", savedOrder.getOrderId(), product.getId());
        log.info("Finished saving of order products for order ID {} and product ID {}", request.getProductId(), product.getId());
        return orderProducts;
    }

    @Override
    public OrderDto update(Integer id, CreateOrderRequest createOrderRequest, HttpServletRequest request) {
        log.info("Starting updating order : {}", id);
        var token = request.getHeader("Authorization").substring(7).trim();
        var userId = jwtService.extractUserId(token);
        Order order = orderRepository.findById(id).
                orElseThrow(() -> new DataNotFoundException("Order with ID " + id + " not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException("User with id" + userId + " not found"));
        Customer customer = user.getCustomer();
        if (customer == null) {
            throw new DataNotFoundException("Customer not found");
        }
        if (!order.getCustomer().getCustomerId().equals(customer.getCustomerId())) {
            throw new AccessDeniedException("Customer ID " + userId + " not found");
        }
        Product product = productRepository.findById(createOrderRequest.getProductId())
                .orElseThrow(() -> new DataNotFoundException("Product not found with ID: " + createOrderRequest.getProductId()));

        if (createOrderRequest.getQuantity() > product.getQuantityInStock()) {
            throw new DataNotFoundException("Quantity exceeds stock for product ID: " + createOrderRequest.getProductId());
        }

        BigDecimal totalAmount = calculateTotalAmount(createOrderRequest, order);
        order.setTotalAmount(totalAmount);
        order.setOrderStatus(OrderStatus.PENDING);
        orderMapper.updateOrderProductFromDto(createOrderRequest, order);
        Order savedOrder = orderRepository.save(order);
        saveOrderProducts(createOrderRequest, product, savedOrder);
        OrderDto orderDto = orderMapper.toOrderDtoGetById(savedOrder);
        log.info("Updated order with ID {} for customer ID {}", id, userId);
        log.info("Finished update of order  with ID {} successfully", savedOrder.getOrderId());
        return orderDto;
    }


    @Override
    public OrderDto getCustomerOrder(Integer id) {
        var email= SecurityContextHolder.getContext().getAuthentication().getName();
        var user=userRepository.findByUsername(email)
                .orElseThrow();
       var order= orderRepository.findByOrderIdAndCustomerCustomerId(id, user.getCustomer().getCustomerId())
                .orElseThrow(() -> new DataNotFoundException("Order with ID " + id + " not found"));
        return orderMapper.toOrderDtoGetById(order);
    }

    @Override
    public List<OrderDto> getAll() {
        List<Order> orders = orderRepository.findAll();
        return orderMapper.toOrderDtoList(orders);
    }
}
