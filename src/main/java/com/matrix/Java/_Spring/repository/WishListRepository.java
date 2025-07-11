package com.matrix.Java._Spring.repository;

import com.matrix.Java._Spring.model.entity.User;
import com.matrix.Java._Spring.model.entity.WishList;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WishListRepository extends JpaRepository<WishList, Integer> {
    @EntityGraph(attributePaths = {"products", "products.category"})
    Optional<WishList> findByUser(User user);
}
