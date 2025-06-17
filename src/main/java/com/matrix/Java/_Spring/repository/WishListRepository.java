package com.matrix.Java._Spring.repository;

import com.matrix.Java._Spring.model.entity.Customer;
import com.matrix.Java._Spring.model.entity.Product;
import com.matrix.Java._Spring.model.entity.User;
import com.matrix.Java._Spring.model.entity.WishList;
import com.matrix.Java._Spring.service.WishListService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WishListRepository extends JpaRepository<WishList,Integer> {
    List<WishList> findAllByUser(User user);

    Optional<WishList> findByUser(User user);

}
