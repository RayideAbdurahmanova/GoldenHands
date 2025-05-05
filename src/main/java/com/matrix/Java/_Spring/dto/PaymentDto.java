package com.matrix.Java._Spring.dto;

import com.matrix.Java._Spring.enums.PaymentMethods;
import com.matrix.Java._Spring.enums.PaymentStatus;
import lombok.Data;

@Data
public class PaymentDto {

//    @Column( nullable = false)
    private double amount;

//    @OneToOne
//    @JoinColumn(name = "order_id")
    private Long orderId;

//    @Enumerated(EnumType.STRING)
    private PaymentMethods paymentMethod;

//    @Column(nullable = false)
//    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;



}
