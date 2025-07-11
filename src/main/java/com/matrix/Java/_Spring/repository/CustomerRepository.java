package com.matrix.Java._Spring.repository;

import com.matrix.Java._Spring.model.entity.Customer;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {

//    @EntityGraph(attributePaths = "addressEntity")
    Optional<Customer> findById(Integer id);
}
