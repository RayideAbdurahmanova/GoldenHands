package com.matrix.Java._Spring.service.impl;

import com.matrix.Java._Spring.dto.CreateOrderProductRequest;
import com.matrix.Java._Spring.dto.CreateOrderRequest;
import com.matrix.Java._Spring.dto.OrderDto;
import com.matrix.Java._Spring.enums.OrderStatus;
import com.matrix.Java._Spring.enums.PaymentStatus;
import com.matrix.Java._Spring.exceptions.DataNotFoundException;
import com.matrix.Java._Spring.mapper.OrderMapper;
import com.matrix.Java._Spring.model.entity.*;
import com.matrix.Java._Spring.model.entity.security.JwtClaimsHolder;
import com.matrix.Java._Spring.repository.*;
import com.matrix.Java._Spring.service.OrderService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final CustomerRepository customerRepository;
    private final AddressRepository addressRepository;
    private final ProductRepository productRepository;
    private final OrderProductRepository orderProductRepository;


    @Override
    public List<OrderDto> getListByCustomerId(Integer customerId) {
        Customer customer=customerRepository.findById(customerId)
                .orElseThrow(()->new DataNotFoundException("Data Not Found With ID:"+customerId+" OrderID"));

        List<Order> orders = orderRepository.findAllByCustomer(customer);
        return orderMapper.toOrderDtoList(orders);
    }

    @Override
    public OrderDto getById(Integer id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Order with ID " + id + " not found"));
        return orderMapper.toOrderDtoGetById(order);
    }


    @Override
    public OrderDto create(CreateOrderRequest createOrderRequest) {

       Integer customerId= getCustomerIdFromToken();
       Customer customer=getCustomerById(customerId);
       Address address=saveShippingAddress(createOrderRequest);

       Order order=createNewOrder(customer,address);

       BigDecimal totalAmount=calculateTotalAmount(createOrderRequest.getProducts(),order);

       order.setTotalAmount(totalAmount);

       Order savedOrder=orderRepository.save(order);

       saveOrderProducts(createOrderRequest.getProducts(),savedOrder);

       return orderMapper.toOrderDtoGetById(savedOrder);

    }

    private Integer getCustomerIdFromToken(){
        Claims claims=JwtClaimsHolder.getClaims();
        return (Integer) claims.get("userId");
    }

    private Customer getCustomerById(Integer customerId){
        return customerRepository.findById(customerId)
                .orElseThrow(()-> new DataNotFoundException("Customer not found with ID: " + customerId));
    }

    private Address saveShippingAddress(CreateOrderRequest createOrderRequest){
        Address address = new Address();
        address.setStreet(createOrderRequest.getShippingStreet());
        address.setCity(createOrderRequest.getShippingCity());
        address.setState(createOrderRequest.getShippingState());
        address.setZipCode(createOrderRequest.getShippingZipCode());
        address.setCountry(createOrderRequest.getShippingCountry());
        return addressRepository.save(address);
    }

    private Order createNewOrder(Customer customer, Address address){
        Order order = new Order();
        order.setCustomer(customer);
        order.setShippingAddress(address);
        order.setOrderStatus(OrderStatus.PENDING); // default status
        order.setPaymentStatus(PaymentStatus.PENDING); // default
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());
        return order;
    }


    private BigDecimal calculateTotalAmount(List<CreateOrderProductRequest> productRequests,Order order){
        BigDecimal totalAmount=BigDecimal.ZERO;
        for(CreateOrderProductRequest productRequest: productRequests){
            Product product=productRepository.findById(Math.toIntExact(productRequest.getProductId()))
                    .orElseThrow(() -> new DataNotFoundException("Product not found with ID: " + productRequest.getProductId()));

            BigDecimal price=BigDecimal.valueOf(product.getPrice());
            int quantity=productRequest.getQuantity();
            totalAmount=  totalAmount.add(price.multiply(BigDecimal.valueOf(quantity)));

            OrderProducts orderProduct = new OrderProducts();
            orderProduct.setOrder(order);
            orderProduct.setProduct(product);
            orderProduct.setQuantity(quantity);
            orderProduct.setPrice(price); //mapper
            orderProductRepository.save(orderProduct);
        }
        return totalAmount;
    }

    private void saveOrderProducts(List<CreateOrderProductRequest> createOrderProductRequests, Order savedOrder){
        for (CreateOrderProductRequest productRequest : createOrderProductRequests) {
            Product product = productRepository.findById(Math.toIntExact(productRequest.getProductId()))
                    .orElseThrow(() -> new DataNotFoundException("Product not found with ID: " + productRequest.getProductId()));

            OrderProducts orderProduct = new OrderProducts();
            orderProduct.setOrder(savedOrder);
            orderProduct.setProduct(product);
            orderProduct.setQuantity(productRequest.getQuantity());
            orderProduct.setPrice(BigDecimal.valueOf(product.getPrice()));

            orderProductRepository.save(orderProduct);
        }
    }

    @Override
    public OrderDto update(Integer id, CreateOrderRequest createOrderRequest) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order with ID " + id + " not found"));


        orderMapper.updateOrderProductFromDto(createOrderRequest,order);
        return  orderMapper.toOrderDtoGetById(orderRepository.save(order));
    }

    @Override
    public void delete(Integer id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order with ID " + id + " not found"));
        orderRepository.deleteById(id);
    }
}
