package com.matrix.Java._Spring.model.entity;

import com.matrix.Java._Spring.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
@ToString(exclude = {"orderProducts", "address", "customer", "seller"})
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "orderId")
    private Integer orderId;

    @Column(name = "order_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderProducts> orderProducts;

    private BigDecimal totalAmount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id")
    private Address address;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
