package com.matrix.Java._Spring.model.entity;

import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.Data;
import lombok.ToString;

@Entity
@Table(name = "addresses")
@Data
@ToString(exclude = {"user", "customer", "seller"})
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String city;
    private String street;
    private String zipCode;

    @OneToOne(mappedBy = "address")
    private User user;

    @OneToOne(mappedBy = "addressEntity", cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id")
    private Seller seller;
}
