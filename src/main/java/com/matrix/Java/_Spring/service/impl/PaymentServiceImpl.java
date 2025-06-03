package com.matrix.Java._Spring.service.impl;


import com.matrix.Java._Spring.dto.CreatePaymentRequest;
import com.matrix.Java._Spring.dto.PaymentDto;
import com.matrix.Java._Spring.enums.PaymentStatus;
import com.matrix.Java._Spring.exceptions.DataNotFoundException;
import com.matrix.Java._Spring.jwt.JwtService;
import com.matrix.Java._Spring.mapper.PaymentMapper;
import com.matrix.Java._Spring.model.entity.Customer;
import com.matrix.Java._Spring.model.entity.Order;
import com.matrix.Java._Spring.model.entity.Payment;
import com.matrix.Java._Spring.repository.CustomerRepository;
import com.matrix.Java._Spring.repository.OrderRepository;
import com.matrix.Java._Spring.repository.PaymentRepository;
import com.matrix.Java._Spring.service.PaymentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentMapper paymentMapper;
    private final PaymentRepository paymentRepository;
    private final CustomerRepository customerRepository;
    private final OrderRepository orderRepository;
    private final JwtService jwtService;


    @Override
    public List<PaymentDto> getListByCustomerId(Integer customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new DataNotFoundException("Customer Not Found With " + customerId));
//        List<Payment> payments=paymentRepository.findByCustomer(customer);
        return paymentMapper.getPaymentDtoList(null);
    }

    @Override
    public PaymentDto getByOrderId(Long orderId) {
        Order order = orderRepository.findById(Math.toIntExact(orderId))
                .orElseThrow(() -> new DataNotFoundException("Order not found"));

//        Payment payment=paymentRepository.findByOrder(order);
        return paymentMapper.toPaymentDtoGetById(null);
    }

    @Override
    public PaymentDto getById(Integer id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Data Not Found With ID:" + id + " PaymentID"));
        return paymentMapper.toPaymentDtoGetById(payment);
    }

    @Override
    public PaymentDto create(CreatePaymentRequest createPaymentRequest, HttpServletRequest request) {
        var token=request.getHeader("Authorization").substring(7).trim();
        var userId =jwtService.extractUserId(token);
        Customer customer=customerRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException("Customer Not Found With " + userId));
        Order order = orderRepository.findById(Math.toIntExact(createPaymentRequest.getOrderId()))
                .orElseThrow(() -> new DataNotFoundException("Order not found"));
        if(!order.getCustomer().getCustomerId().equals(userId)){
            throw new DataNotFoundException("Order does not belong to customer with ID " + userId);
        }
        Payment payment = buildNewPayment(createPaymentRequest, order);
        Payment saved = paymentRepository.save(payment);
        log.info("Created payment with ID {} for order ID {}", saved.getPaymentId(), order.getOrderId());
        return paymentMapper.toPaymentDtoGetById(saved);
    }

    private Payment buildNewPayment(CreatePaymentRequest createPaymentRequest, Order order) {
        Payment payment = new Payment();
        payment.setAmount(BigDecimal.valueOf(createPaymentRequest.getAmount()));
        payment.setPaymentMethod(createPaymentRequest.getPaymentMethod());
        payment.setPaymentStatus(PaymentStatus.PENDING);//default
        return payment;
    }

    @Override
    public PaymentDto update(Integer id, CreatePaymentRequest createPaymentRequest, HttpServletRequest request) {
        var token=request.getHeader("Authorization").substring(7).trim();
        var userId =jwtService.extractUserId(token);
        Payment payment=paymentRepository.findById(id).
                orElseThrow(() -> new DataNotFoundException("Payment Not Found With ID:" + id + " PaymentID"));

        if(!payment.getCustomer().getCustomerId().equals(userId)){
            throw new DataNotFoundException("Payment does not belong to customer with ID " + userId);
        }
        payment.setAmount(BigDecimal.valueOf(createPaymentRequest.getAmount()));
        payment.setPaymentMethod(createPaymentRequest.getPaymentMethod());
        if (createPaymentRequest.getPaymentStatus()==null){
            payment.setPaymentStatus(PaymentStatus.valueOf(String.valueOf(createPaymentRequest.getPaymentStatus())));
        }

        Payment update = paymentMapper.toCreatePaymentRequest(createPaymentRequest);
        log.info("Updated payment with ID {} for customer ID {}", id, userId);
        return paymentMapper.toPaymentDtoGetById(paymentRepository.save(update));

    }

    @Override
    public void delete(Integer id,HttpServletRequest request) {
        var token=request.getHeader("Authorization").substring(7).trim();
        var userId =jwtService.extractUserId(token);
        Payment payment=paymentRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Payment Not Found With ID:" + id + " PaymentID"));
     if(!payment.getCustomer().getCustomerId().equals(userId)){
         throw new DataNotFoundException("Payment does not belong to customer with ID " + userId);
     }
        paymentRepository.deleteById(id);
        log.info("Deleted payment with ID {} for customer ID {}", id, userId);

    }
}
