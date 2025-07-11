package com.matrix.Java._Spring.repository;

import com.matrix.Java._Spring.model.entity.Customer;
import com.matrix.Java._Spring.model.entity.Order;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order,Integer> {

        @EntityGraph(attributePaths = {"address","orderProducts"})
    List<Order> findAllByCustomer(Customer customer);

    @EntityGraph(attributePaths = {"address","orderProducts"})
    Optional<Order> findByOrderIdAndCustomerCustomerId(Integer orderId,Integer customerId);
}
