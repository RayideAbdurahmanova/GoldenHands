package com.matrix.Java._Spring.dto;


import lombok.Data;

import java.util.List;

@Data
public class ProductDto {

//    @Column( nullable = false)
    private String productName;

//    @Column( nullable = false)
    private double price;

//    @ManyToOne
//    @JoinColumn(name = "category_id",nullable = false)
    private Long categoryId;

//    @ManyToOne
//    @JoinColumn(name = "seller_id",nullable = false)
    private Long sellerId;

    private String description;

//    @Column( nullable = false)
    private Integer quantityInStock;

//    @OneToMany(mappedBy = "product")
    private List<ReviewsDto> reviews;





}
