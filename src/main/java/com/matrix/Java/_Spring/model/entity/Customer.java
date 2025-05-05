package com.matrix.Java._Spring.model.entity;

import com.matrix.Java._Spring.enums.UserRole;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name="customers")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Integer customerId;

    @Column(name="name")
    private String name;

    @Column(name = "username")
    private String username;

    private String email;

    @Column(name = "password_hash", nullable = false)
    private String password;

    @Column( nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole role;

    @OneToMany(mappedBy = "customer",cascade = CascadeType.ALL)
    private List<Order> orders;

    @OneToMany(mappedBy = "seller", cascade = CascadeType.ALL)
    private List<Product> products;

    @OneToMany(mappedBy = "customer")
    private List<WishList> wishLists;

    @OneToMany(mappedBy = "customer")
    private List<Reviews> reviews;

    @OneToMany(mappedBy = "customer")
    private List<Address> addresses;

}
