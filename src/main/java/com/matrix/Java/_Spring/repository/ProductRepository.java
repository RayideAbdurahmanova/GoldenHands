package com.matrix.Java._Spring.repository;

import com.matrix.Java._Spring.model.entity.Category;
import com.matrix.Java._Spring.model.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ProductRepository extends JpaRepository<Product,Integer> {
//    List<Product> findByOrder(Order order);

    List<Product> findByCategory(Category category);
}
