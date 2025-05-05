package com.matrix.Java._Spring.model.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "addresses")
@Data
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String country;
    private String city;
    private String street;
    private String zipCode;
    private String state;

    @ManyToOne
    @JoinColumn(name = "customer_id") // adjust name if needed
    private Customer customer;





}
