package com.matrix.Java._Spring.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Entity
@Data
@Table(name = "customers")
@ToString(exclude = { "orders", "user","addressEntity"})
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer customerId;

    @OneToOne
    @JoinColumn(name = "address_id")
    private Address addressEntity;

    @OneToMany(mappedBy = "customer")
    private List<Order> orders;

    @OneToOne(mappedBy = "customer")
    private User user;
}
