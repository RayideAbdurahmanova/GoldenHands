package com.matrix.Java._Spring.model.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name="wishlist")
@Data
public class WishList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int wishlistId;

    @ManyToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private Product product;

}
