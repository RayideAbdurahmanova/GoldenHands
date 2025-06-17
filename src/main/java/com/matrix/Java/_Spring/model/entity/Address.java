package com.matrix.Java._Spring.model.entity;

import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.Data;

@Entity
@Table(name = "addresses")
@Data
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String city;
    private String street;
    private String zipCode;

    @OneToOne(mappedBy = "address")
    private User user;

    @OneToOne(mappedBy = "addressEntity", cascade = CascadeType.ALL)
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "seller_id")
    private Seller seller;
}
