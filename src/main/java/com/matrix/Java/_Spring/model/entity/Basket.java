package com.matrix.Java._Spring.model.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "basket")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString(exclude = {"customer", "items"})
public class Basket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(nullable = false)
    BigDecimal totalPrice = BigDecimal.ZERO;

    @Column(nullable = false)
    LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable = false)
    LocalDateTime updatedAt = LocalDateTime.now();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    Customer customer;

    @OneToMany(mappedBy = "basket", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    List<BasketItem> items = new ArrayList<>();
}
