package com.matrix.Java._Spring.repository;


import com.matrix.Java._Spring.model.entity.Basket;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BasketRepository extends JpaRepository<Basket, Long> {

    @EntityGraph(attributePaths = "items")
    Basket findByCustomer_CustomerId(Integer userId);

}
