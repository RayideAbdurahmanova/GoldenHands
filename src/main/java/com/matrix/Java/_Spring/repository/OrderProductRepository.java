package com.matrix.Java._Spring.repository;

import com.matrix.Java._Spring.model.entity.Order;
import com.matrix.Java._Spring.model.entity.OrderProducts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderProductRepository extends JpaRepository<OrderProducts, Integer> {


    List<OrderProducts> findByOrder(Order order);

    @Query("SELECT op FROM OrderProducts op WHERE op.order.id = :orderId AND op.product.id = :productId")
    Optional<OrderProducts> findByOrderIdAndProductId(@Param("orderId") Integer orderId, @Param("productId") Integer productId);
}
