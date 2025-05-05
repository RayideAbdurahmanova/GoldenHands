package com.matrix.Java._Spring.service;

import com.matrix.Java._Spring.dto.CreateProductRequest;
import com.matrix.Java._Spring.dto.ProductDto;

import java.util.List;

public interface ProductService {

    List<ProductDto> getList();

    ProductDto getById(Integer id);

    ProductDto create(CreateProductRequest createProductRequest);

    ProductDto update(Integer id, CreateProductRequest createProductRequest);

    void delete(Integer id);

    List<ProductDto> getProductsByCategory(Integer categoryId);
}
