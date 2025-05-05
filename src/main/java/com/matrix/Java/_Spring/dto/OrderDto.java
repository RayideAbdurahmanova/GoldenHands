package com.matrix.Java._Spring.dto;

import com.matrix.Java._Spring.enums.OrderStatus;
import com.matrix.Java._Spring.enums.PaymentStatus;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderDto {

    private Long customerId;

    private OrderStatus orderStatus;

    private List<OrderProductDto> orderProducts;

    private BigDecimal totalAmount;

    private PaymentStatus paymentStatus;

    private AddressDto shippingAddress;




}
