package com.matrix.Java._Spring.model.entity;


import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name="products")
@Data
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Integer id;

    @Column( nullable = false)
    private String productName;

    @Column( nullable = false)
    private double price;


    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private Category category;


    @ManyToOne
    @JoinColumn(name = "seller_id", referencedColumnName = "id")
    private Customer seller;

    @OneToMany(mappedBy = "product")
    private List<OrderProducts> orderProducts;

    private String description;

    @Column( nullable = false,name="stock_quantity")
    private Integer quantityInStock;

    @Column(name = "image_url")
    private String imageUrl;


    @OneToMany(mappedBy = "product")
    private List<Reviews> reviews;

    @OneToMany(mappedBy = "product")
    private List<WishList> wishLists;
}
