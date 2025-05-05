package com.matrix.Java._Spring.model.entity;

import com.matrix.Java._Spring.enums.PaymentMethods;
import com.matrix.Java._Spring.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name="payments")
@Data
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Integer paymentId;

    @Column( nullable = false)
    private BigDecimal amount;  // Ödənilən məbləğ

    @OneToOne
    @JoinColumn(name = "order_id", referencedColumnName = "id")
    private Order order;

    @Enumerated(EnumType.STRING)
    private PaymentMethods paymentMethod;

    private LocalDateTime paymentDate;  // Ödəniş tarixi

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;


    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;
}
