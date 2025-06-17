package com.matrix.Java._Spring.repository;

import com.matrix.Java._Spring.model.entity.Product;
import com.matrix.Java._Spring.model.entity.Reviews;
import com.matrix.Java._Spring.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewsRepository extends JpaRepository<Reviews, Integer> {

    List<Reviews> findByProduct(Product product);

    boolean existsByUserAndProduct(User user, Product product);

    Optional<Reviews> findByUserIdAndProductId(Integer userId, Integer productId);

    List<Reviews> findByUser(User user);
}
