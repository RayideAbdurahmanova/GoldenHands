package com.matrix.Java._Spring.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name="categories")
@Data
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Integer id;

    @Column(nullable = false,name="category_name")
    private String categoryName;

    @ManyToOne
    @JoinColumn(name = "parent_category_id")
    @JsonBackReference
    private Category parentCategory;


    @OneToMany(mappedBy = "parentCategory")
    private List<Category> subCategories;

    @OneToMany(mappedBy = "category")
    private List<Product> products;

}
