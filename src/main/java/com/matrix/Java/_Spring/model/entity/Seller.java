package com.matrix.Java._Spring.model.entity;


import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "seller")
@ToString(exclude = { "user"})
public class Seller {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String storeName;
    private String businessAddress;

    @OneToMany(mappedBy = "seller")
    List<Product> products;

//    @OneToMany(mappedBy = "seller")
//    List<Order> orders;

//    @OneToMany(mappedBy = "seller")
//    List<Address> addresses;

//    @OneToMany(mappedBy = "seller")
//    List<Reviews> reviews;

    @OneToOne(mappedBy = "seller")
    private User user;

//    @OneToOne(mappedBy = "seller", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    private Balance balance;
}
