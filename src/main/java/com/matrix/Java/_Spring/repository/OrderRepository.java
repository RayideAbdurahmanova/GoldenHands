package com.matrix.Java._Spring.repository;

import com.matrix.Java._Spring.model.entity.Customer;
import com.matrix.Java._Spring.model.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order,Integer> {

    List<Order> findAllByCustomer(Customer customer);
}
