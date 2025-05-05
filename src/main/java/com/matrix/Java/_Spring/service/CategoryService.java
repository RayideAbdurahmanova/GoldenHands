package com.matrix.Java._Spring.service;

import com.matrix.Java._Spring.dto.CreateCategoryRequest;
import com.matrix.Java._Spring.dto.CategoryDto;
import java.util.List;

public interface CategoryService {

    List<CategoryDto> getList();

    CategoryDto getById(Integer id);

    CategoryDto create(CreateCategoryRequest createCategoryRequest);

    CategoryDto update(Integer id, CreateCategoryRequest createCategoryRequest);

    void delete(Integer id);

    List<CategoryDto> getSubcategories(Integer parentId);

//    List<ProductDto> getCategoriesWithProducts(P)

    Integer getCountOfProductsInCategory(Integer id);

}
