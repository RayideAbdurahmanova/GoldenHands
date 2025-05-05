package com.matrix.Java._Spring.model.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name="reviews")
@Data
public class Reviews {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Integer reviewId;

    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    private Customer customer;

    @Column( nullable = false)
    private Integer rating;

    private String comment;

}
