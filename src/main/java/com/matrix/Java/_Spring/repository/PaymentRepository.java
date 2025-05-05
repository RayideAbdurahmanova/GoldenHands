package com.matrix.Java._Spring.repository;

import com.matrix.Java._Spring.dto.PaymentDto;
import com.matrix.Java._Spring.model.entity.Customer;
import com.matrix.Java._Spring.model.entity.Order;
import com.matrix.Java._Spring.model.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment,Integer> {

    List<Payment> findByCustomer(Customer customer);

//    Optional<Payment> findByOrderId(Long orderId);

    Payment findByOrder(Order order);
}
