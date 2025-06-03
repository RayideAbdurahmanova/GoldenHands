package com.matrix.Java._Spring.repository;

import com.matrix.Java._Spring.model.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Integer> {
    Boolean existsByCategoryName(String name);

    List<Category> findByParentCategory(Category parentCategory);
}
