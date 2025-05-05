package com.matrix.Java._Spring.model.entity;

import com.matrix.Java._Spring.enums.OrderStatus;
import com.matrix.Java._Spring.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer orderId;

    @ManyToOne
    @JoinColumn(name = "buyer_id", referencedColumnName = "id",nullable = false)
    private Customer customer;

    @Column(name="order_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @OneToMany(mappedBy = "order")
    private List<OrderProducts> orderProducts;

    @OneToOne(mappedBy = "order")
    private Payment payment;

    private BigDecimal totalAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", nullable = false)
    private PaymentStatus paymentStatus;

    @OneToOne
    @JoinColumn(name="shipping_address_id",nullable = false)
    private Address shippingAddress;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


}
