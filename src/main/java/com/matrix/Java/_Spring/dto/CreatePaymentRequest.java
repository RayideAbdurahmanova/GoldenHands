package com.matrix.Java._Spring.dto;

import com.matrix.Java._Spring.enums.PaymentMethods;
import com.matrix.Java._Spring.enums.PaymentStatus;
import lombok.Data;

@Data
public class CreatePaymentRequest {

    private double amount;
    private Long orderId;
    private PaymentMethods paymentMethod;
    private PaymentStatus paymentStatus;
}
