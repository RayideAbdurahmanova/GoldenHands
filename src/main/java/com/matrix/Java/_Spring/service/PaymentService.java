package com.matrix.Java._Spring.service;

import com.matrix.Java._Spring.dto.CreatePaymentRequest;
import com.matrix.Java._Spring.dto.PaymentDto;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface PaymentService {

    List<PaymentDto> getListByCustomerId(Integer customerId);

    PaymentDto getByOrderId(Long orderId);

    PaymentDto getById(Integer id);

    PaymentDto create(CreatePaymentRequest createPaymentRequest, HttpServletRequest request);

    PaymentDto update(Integer id, CreatePaymentRequest createPaymentRequest,HttpServletRequest request);

    void delete(Integer id,HttpServletRequest request);
}
