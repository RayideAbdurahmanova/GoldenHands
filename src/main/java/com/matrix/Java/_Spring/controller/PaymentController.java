package com.matrix.Java._Spring.controller;


import com.matrix.Java._Spring.dto.CreatePaymentRequest;
import com.matrix.Java._Spring.dto.PaymentDto;
import com.matrix.Java._Spring.service.PaymentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService paymentService;


    @GetMapping("/customer/{customerId}")
    public List<PaymentDto> getListByCustomerId(@PathVariable Integer customerId) {
        return paymentService.getListByCustomerId(customerId);
    }


    @GetMapping("/{id}")
    public PaymentDto getById(@PathVariable Integer id) {
        return  paymentService.getById(id);
    }

    @GetMapping("/order/{orderId}")
    public PaymentDto getByOrderId(@PathVariable Long orderId) {
        return paymentService.getByOrderId(orderId);
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PaymentDto create(@RequestBody CreatePaymentRequest createPaymentRequest, HttpServletRequest request) {
        return paymentService.create(createPaymentRequest,request);
    }


    @PutMapping("/{id}")
    public PaymentDto update(@PathVariable Integer id,
                             @RequestBody CreatePaymentRequest createPaymentRequest,
                             HttpServletRequest request) {
       return paymentService.update(id,createPaymentRequest,request);
    }


    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id,
                       HttpServletRequest request) {
        paymentService.delete(id,request);
    }

}
