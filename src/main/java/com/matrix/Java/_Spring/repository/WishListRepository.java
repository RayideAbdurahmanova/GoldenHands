package com.matrix.Java._Spring.repository;

import com.matrix.Java._Spring.model.entity.Customer;
import com.matrix.Java._Spring.model.entity.WishList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WishListRepository extends JpaRepository<WishList,Integer> {
    List<WishList> findAllByCustomer(Customer customer);
}
