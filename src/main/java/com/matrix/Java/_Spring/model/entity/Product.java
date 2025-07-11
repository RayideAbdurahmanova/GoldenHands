package com.matrix.Java._Spring.model.entity;


import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Entity
@Table(name = "products")
@Data
@ToString(exclude = {"category", "seller", "orderProducts", "reviews"})
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(nullable = false)
    private String productName;

    @Column(nullable = false)
    private double price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "seller_id")
    private Seller seller;

    @OneToMany(mappedBy = "product")
    private List<OrderProducts> orderProducts;

    private String description;

    @Column(nullable = false, name = "stock_quantity")
    private Integer quantityInStock;

    @Column(name = "image_url")
    private String imageUrl;

    @OneToMany(mappedBy = "product")
    private List<Reviews> reviews;
}
