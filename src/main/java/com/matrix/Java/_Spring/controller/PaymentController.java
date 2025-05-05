package com.matrix.Java._Spring.controller;


import com.matrix.Java._Spring.dto.CreatePaymentRequest;
import com.matrix.Java._Spring.dto.PaymentDto;
import com.matrix.Java._Spring.exceptions.DataNotFoundException;
import com.matrix.Java._Spring.model.entity.Order;
import com.matrix.Java._Spring.model.entity.Payment;
import com.matrix.Java._Spring.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService paymentService;


    @GetMapping("/ByCustomerId/{customerId}")
    public List<PaymentDto> getListByCustomerId(@PathVariable Integer customerId) {
        return paymentService.getListByCustomerId(customerId);
    }


    @GetMapping("/ByPaymentId/{id}")
    public PaymentDto getById(@PathVariable Integer id) {
        return  paymentService.getById(id);
    }

    @GetMapping("ByOrderId/{orderId}")
    public PaymentDto getByOrderId(@PathVariable Long orderId) {
        return paymentService.getByOrderId(orderId);
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PaymentDto create(@RequestBody CreatePaymentRequest createPaymentRequest) {
        return paymentService.create(createPaymentRequest);
    }


    @PutMapping("/update/{id}")
    public PaymentDto update(@PathVariable Integer id,
                             @RequestBody CreatePaymentRequest createPaymentRequest) {
       return paymentService.update(id,createPaymentRequest);
    }


    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable Integer id) {
        paymentService.delete(id);
    }

}
