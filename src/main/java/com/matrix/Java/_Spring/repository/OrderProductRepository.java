package com.matrix.Java._Spring.repository;

import com.matrix.Java._Spring.model.entity.Order;
import com.matrix.Java._Spring.model.entity.OrderProducts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderProductRepository extends JpaRepository<OrderProducts,Integer> {
    List<OrderProducts> findByOrder(Order order);

}
