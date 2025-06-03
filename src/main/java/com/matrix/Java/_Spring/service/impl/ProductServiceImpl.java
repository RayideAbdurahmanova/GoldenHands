package com.matrix.Java._Spring.service.impl;


import com.matrix.Java._Spring.dto.CreateProductRequest;
import com.matrix.Java._Spring.dto.ProductDto;
import com.matrix.Java._Spring.exceptions.DataNotFoundException;
import com.matrix.Java._Spring.jwt.JwtService;
import com.matrix.Java._Spring.mapper.ProductMapper;
import com.matrix.Java._Spring.model.entity.Category;
import com.matrix.Java._Spring.model.entity.Product;
import com.matrix.Java._Spring.repository.*;
import com.matrix.Java._Spring.service.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final CategoryRepository categoryRepository;
    private final CustomerRepository customerRepository;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Override
    public List<ProductDto> getList() {
        log.info("Starting retrieval list of products");
        List<ProductDto> productDtoList = productMapper.getProductDtoList(productRepository.findAll());
        log.info("Finished retrieval {} products",productDtoList.size());
        return productDtoList;
    }

    @Override
    public ProductDto getById(Integer id) {
        log.info("Starting retrieval of product by id: {}", id);
        Product product=productRepository.findById(id).orElseThrow(
                ()->new DataNotFoundException("Data not found with ID:" + id)
        );
        ProductDto productDto=productMapper.toProductDtoGetById(product);
        log.info("Finished retrieval {} product  successfully", productDto);
        return productDto;
    }

    @Override
    public ProductDto create(CreateProductRequest createProductRequest, HttpServletRequest request) {
        log.info("Starting creation of product: {}", createProductRequest);
        var token = request.getHeader("Authorization").substring(7).trim();
        var userId = jwtService.extractUserId(token);
        var user = userRepository.findById(userId).orElseThrow();
        var category = categoryRepository.findById(Math.toIntExact(createProductRequest.getCategoryId()))
                .orElseThrow(() -> new DataNotFoundException("Category Not Found With ID:" + createProductRequest.getCategoryId()));
        Product product = productMapper.toCreateProductRequest(createProductRequest);
        product.setSeller(user.getSeller());
        product.setCategory(category);
        Product saved = productRepository.save(product);
        log.info("Created product with ID {} by seller ID {}", saved.getId(), userId);
        ProductDto productDto=productMapper.toProductDtoGetById(saved);
        log.info("Finished creation of product with id: {}", saved.getId());
        return productDto;
    }

    @Override
    public ProductDto update(Integer id, CreateProductRequest createProductRequest, HttpServletRequest request) {
        log.info("Starting update of product: {}", createProductRequest);
        var token = request.getHeader("Authorization").substring(7).trim();
        var sellerId = jwtService.extractUserId(token);
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product with ID " + id + " not found"));
        if(!product.getSeller().getId().equals(sellerId)) {
            throw new DataNotFoundException("Product with ID " + id + " not found");
        }
        Category category=categoryRepository.findById(Math.toIntExact(createProductRequest.getCategoryId()))
                .orElseThrow(()->new DataNotFoundException("Category not found"));
        productMapper.updateProductFromRequest(createProductRequest,product);
        product.setCategory(category);
        Product updated = productRepository.save(product);
        log.info("Updated product with ID {} by seller ID {}", id, sellerId);
        ProductDto productDto=productMapper.toProductDtoGetById(updated);
        log.info("Finished update of product with ID {} successfully", updated.getId());
        return productDto;
    }


    @Override
    public void delete(Integer id, HttpServletRequest request) {
        log.info("Starting deletion of product: {}", id);
        var token = request.getHeader("Authorization").substring(7).trim();
        var sellerId = jwtService.extractUserId(token);
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product with ID " + id + " not found"));
        if(!product.getSeller().getId().equals(sellerId)) {
            throw new DataNotFoundException("Product with ID " + id + " not found");
        }
        productRepository.deleteById(id);
        log.info("Deleted product with ID {} by seller ID {}", id, sellerId);
        log.info("Finished deletion of product with id: {}", id);
    }

    @Override
    public List<ProductDto> getProductsByCategory(Integer categoryId) {
        log.info("Starting retrieval of products by category: {}", categoryId);
        Category category = categoryRepository.findById(categoryId).orElseThrow(
                () -> new DataNotFoundException("Category not found with: " + categoryId)
        );
        List<Product> product = productRepository.findByCategory(category);
        List<ProductDto> productDtoList=productMapper.getProductDtoList(product);
        log.info("Finished retrieval of products by category: {}", categoryId);
        return productDtoList;
    }
}
