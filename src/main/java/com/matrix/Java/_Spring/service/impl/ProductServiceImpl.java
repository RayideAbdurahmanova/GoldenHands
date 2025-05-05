package com.matrix.Java._Spring.service.impl;


import com.matrix.Java._Spring.dto.CreateProductRequest;
import com.matrix.Java._Spring.dto.ProductDto;
import com.matrix.Java._Spring.exceptions.DataNotFoundException;
import com.matrix.Java._Spring.mapper.ProductMapper;
import com.matrix.Java._Spring.model.entity.Product;
import com.matrix.Java._Spring.repository.ProductRepository;
import com.matrix.Java._Spring.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Override
    public List<ProductDto> getList() {
        return productMapper.getProductDtoList(productRepository.findAll());
    }

    @Override
    public ProductDto getById(Integer id) {
        return productMapper.toProductDtoGetById(productRepository.findById(id).orElseThrow(
                ()->new DataNotFoundException("Data Not Found With ID:"+id+" ProductID")
        ));
    }

    @Override
    public ProductDto create(CreateProductRequest createProductRequest) {
        Product product=productMapper.toCreateProductRequest(createProductRequest);
        return productMapper.toProductDtoGetById(productRepository.save(product));
    }

    @Override
    public ProductDto update(Integer id, CreateProductRequest createProductRequest) {

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product with ID " + id + " not found"));

        Product update=productMapper.toCreateProductRequest(createProductRequest);
        return  productMapper.toProductDtoGetById(productRepository.save(update));
    }


    @Override
    public void delete(Integer id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product with ID " + id + " not found"));

        productRepository.deleteById(id);
    }
}
