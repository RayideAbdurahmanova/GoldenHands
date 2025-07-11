package com.matrix.Java._Spring.service;

import com.matrix.Java._Spring.dto.CreateProductRequest;
import com.matrix.Java._Spring.dto.ProductDto;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface ProductService {

    List<ProductDto> getList();

    ProductDto getById(Integer id);

    ProductDto create(CreateProductRequest createProductRequest, HttpServletRequest request);

    ProductDto update(Integer id, CreateProductRequest createProductRequest, HttpServletRequest request);

    void delete(Integer id, HttpServletRequest request);

    List<ProductDto> getProductsByCategory(Integer categoryId);

    List<ProductDto> getProductsBySeller(Integer sellerId);
}
