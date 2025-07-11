package com.matrix.Java._Spring.service.impl;

import com.matrix.Java._Spring.client.WalletClient;
import com.matrix.Java._Spring.dto.CreateOrderRequest;
import com.matrix.Java._Spring.dto.OrderDto;
import com.matrix.Java._Spring.dto.PaymentRequest;
import com.matrix.Java._Spring.enums.OrderStatus;
import com.matrix.Java._Spring.exceptions.DataNotFoundException;
import com.matrix.Java._Spring.jwt.JwtService;
import com.matrix.Java._Spring.mapper.OrderMapper;
import com.matrix.Java._Spring.mapper.OrderProductMapper;
import com.matrix.Java._Spring.mapper.ProductMapperImpl;
import com.matrix.Java._Spring.model.entity.*;
import com.matrix.Java._Spring.repository.*;
import com.matrix.Java._Spring.service.AddressService;
import com.matrix.Java._Spring.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private final WalletClient walletClient;
    private final BasketRepository basketRepository;
    private final ProductMapperImpl productMapperImpl;


    @Override
    public List<OrderDto> getListByCustomerId() {
        var email = SecurityContextHolder.getContext().getAuthentication().getName();
        var user = userRepository.findByUsername(email)
                .orElseThrow();
        log.info("Starting retrieval of order list with ID: {}", user.getCustomer().getCustomerId());
        Customer customer = customerRepository.findById(user.getCustomer().getCustomerId())
                .orElseThrow(() -> new DataNotFoundException("Data Not Found With ID:" + user.getCustomer().getCustomerId() + " OrderID"));

        List<Order> orders = orderRepository.findAllByCustomer(customer);
        return orderMapper.toOrderDtoList(orders);
    }

    @Override
    public OrderDto getById(Integer id) {
        log.info("Starting retrieval of order with ID: {}", id);
        if (id <= 0) {
            throw new DataNotFoundException("ID must be positive");
        }
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
        log.debug("Extracted token: {}", token);
        var userId = jwtService.extractUserId(token);
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException("User with ID " + userId + " not found"));

        Customer customer = customerRepository.findById(user.getCustomer().getCustomerId())
                .orElseThrow(() -> new DataNotFoundException("Customer not found"));

        var basket = basketRepository.findByCustomer_CustomerId(user.getCustomer().getCustomerId());
        if (basket.getItems().isEmpty()) {
            throw new DataNotFoundException("Basket is empty ID with user ID : " + userId);
        }

        addressService.create(createOrderRequest.getCreateAddress(), request);
        Order order = createNewOrder(createOrderRequest, user, basket);

        order.setOrderStatus(PENDING);
        order.setCustomer(customer);
        customer.setAddressEntity(user.getAddress());
        order.setTotalAmount(basket.getTotalPrice());
        Order savedOrder = orderRepository.save(order);
        var orderProducts = saveOrderProductsFromBasket(basket, savedOrder);
        log.info("Created order with ID {} for customer ID {}", savedOrder.getOrderId(), user.getCustomer().getCustomerId());
        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setOrderId(savedOrder.getOrderId());
        paymentRequest.setAmount(basket.getTotalPrice());
        paymentRequest.setCustomerId(user.getCustomer().getCustomerId());

        if (paymentRequest.getOrderId() == null || paymentRequest.getAmount() == null || paymentRequest.getCustomerId() == null) {
            log.error("Invalid PaymentRequest: orderId={}, amount={}, customerId={}",
                    paymentRequest.getOrderId(), paymentRequest.getAmount(), paymentRequest.getCustomerId());
            throw new IllegalArgumentException("PaymentRequest contains null fields");
        }

        try {
            walletClient.pay(request.getHeader("Authorization"), paymentRequest);
            savedOrder.setOrderStatus(OrderStatus.SHIPPED);
            log.info("Payment successful for order ID: {}. Order status updated to SHIPPED", savedOrder.getOrderId());

        } catch (Exception e) {
            savedOrder.setOrderStatus(OrderStatus.CANCELED);
            log.error("Payment failed for order ID: {}. Order status updated to CANCELED", savedOrder.getOrderId());
            throw new RuntimeException(e.getMessage());
        }
        basketRepository.delete(basket);
        OrderDto orderDto = orderMapper.toOrderDtoGetById(savedOrder);
        var orderProductDto = orderProducts.stream()
                .map(orderProductMapper::toOrderProductDtoGetById)
                .toList();

        log.info("Finished creation of order  with id: {}", savedOrder.getOrderId());
        orderDto.setOrderProducts(orderProductDto);
        return orderDto;
    }


    private Order createNewOrder(CreateOrderRequest createOrderRequest, User user, Basket basket) {
        log.info("Starting creation of new order for address: {}",
                createOrderRequest.getCreateAddress() != null ? createOrderRequest.getCreateAddress() : null);
        Customer customer = customerRepository.findById(user.getCustomer().getCustomerId())
                .orElseThrow(() -> new DataNotFoundException("Customer not found"));

        Order order = orderMapper.toOrder(createOrderRequest, user, basket);
        order.setCustomer(customer);
        return order;
    }

    List<OrderProducts> saveOrderProductsFromBasket(Basket basket, Order order) {
        log.info("Starting saving of order products for order ID: {}", order.getOrderId());
        List<OrderProducts> orderProductsList = basket.getItems().stream()
                .map(basketItem -> {
                    Product productEntity = productRepository.findById(basketItem.getProduct().getId())
                            .orElseThrow(() -> new DataNotFoundException("Product not found with ID: " + basketItem.getProduct().getId()));

                    if (productEntity.getQuantityInStock() < basketItem.getQuantity()) {
                        throw new DataNotFoundException("Product Quantity Exceeded Stock");
                    }
                    OrderProducts orderProducts = new OrderProducts();
                    orderProducts.setProduct(productEntity);
                    orderProducts.setOrder(order);
                    orderProducts.setQuantity(basketItem.getQuantity());
                    orderProducts.setPrice(BigDecimal.valueOf(productEntity.getPrice()));

                    productEntity.setQuantityInStock(productEntity.getQuantityInStock() - basketItem.getQuantity());
                    productRepository.save(productEntity);

                    OrderProducts savedOrderProducts = orderProductRepository.save(orderProducts);

                    log.info("Saved order product for order ID {} and product ID {}",
                            order.getOrderId(), productEntity.getId());

                    return savedOrderProducts;
                }).toList();

        order.setOrderProducts(orderProductsList);
        log.info("Finished saving of order products for order ID {}", order.getOrderId());
        return orderProductsList;
    }


    @Override
    public OrderDto getCustomerOrder(Integer id) {
        if (id <= 0) {
            throw new DataNotFoundException("ID must be positive");
        }
        var email = SecurityContextHolder.getContext().getAuthentication().getName();
        var user = userRepository.findByUsername(email)
                .orElseThrow();
        var order = orderRepository.findByOrderIdAndCustomerCustomerId(id, user.getCustomer().getCustomerId())
                .orElseThrow(() -> new DataNotFoundException("Order with ID " + id + " not found"));
        return orderMapper.toOrderDtoGetById(order);
    }

    @Override
    public List<OrderDto> getAll() {
        List<Order> orders = orderRepository.findAll();
        return orderMapper.toOrderDtoList(orders);
    }
}
