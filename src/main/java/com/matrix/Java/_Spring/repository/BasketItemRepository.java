package com.matrix.Java._Spring.repository;

import com.matrix.Java._Spring.model.entity.BasketItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BasketItemRepository  extends JpaRepository<BasketItem,Long> {
}
