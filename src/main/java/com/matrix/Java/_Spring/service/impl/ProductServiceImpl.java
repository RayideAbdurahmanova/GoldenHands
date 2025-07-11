package com.matrix.Java._Spring.service.impl;


import com.matrix.Java._Spring.dto.CreateProductRequest;
import com.matrix.Java._Spring.dto.ProductDto;
import com.matrix.Java._Spring.exceptions.DataNotFoundException;
import com.matrix.Java._Spring.jwt.JwtService;
import com.matrix.Java._Spring.mapper.ProductMapper;
import com.matrix.Java._Spring.mapper.ReviewsMapper;
import com.matrix.Java._Spring.mapper.ReviewsMapperImpl;
import com.matrix.Java._Spring.model.entity.Category;
import com.matrix.Java._Spring.model.entity.Product;
import com.matrix.Java._Spring.repository.*;
import com.matrix.Java._Spring.service.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    private final ReviewsMapper reviewsMapper;

    @Override
    public List<ProductDto> getList() {
        log.info("Starting retrieval list of products");

        List<Product> products = productRepository.findAll();
        List<ProductDto> productDtoList = reviewsMapper.getProductDtoList(products);
        productDtoList.stream()
                .map(productDto -> {
                    Product product = products.stream()
                            .filter(p -> p.getId().equals(productDto.getId()))
                            .findFirst()
                            .orElse(null);

                    if (product != null && product.getCategory() != null && product.getSeller() != null) {
                        productDto.setCategoryId(Long.valueOf(product.getCategory().getId()));
                        productDto.setSellerId(product.getSeller().getId());
                    } else {
                        productDto.setCategoryId(null);
                        productDto.setSellerId(null);
                    }
                    return productDto;
                })
                .toList();


        log.info("Finished retrieval {} products", productDtoList.size());
        return productDtoList;
    }

    @Override
    public ProductDto getById(Integer id) {
        log.info("Starting retrieval of product by id: {}", id);
        if (id <= 0) {
            throw new DataNotFoundException("Product ID must be positive");
        }
        Product product = productRepository.findById(id).orElseThrow(
                () -> new DataNotFoundException("Data not found with ID:" + id)
        );
        ProductDto productDto = productMapper.toProductDtoGetById(product);
        productDto.getReviews().forEach(reviewsDto -> {
            reviewsDto.setProductId(product.getId());
        });

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
        ProductDto productDto = productMapper.toProductDtoGetById(saved);
        log.info("Finished creation of product with id: {}", saved.getId());
        return productDto;
    }

    @Override
    @Transactional
    public ProductDto update(Integer id, CreateProductRequest createProductRequest, HttpServletRequest request) {
        log.info("Starting update of product: {}", createProductRequest);
        if (id <= 0) {
            throw new DataNotFoundException("ID must be positive");
        }
        var token = request.getHeader("Authorization").substring(7).trim();
        var userId = jwtService.extractUserId(token);
        var sellerId = userRepository.findById(userId).get().getSeller().getId();
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product with ID " + id + " not found"));
        if (!product.getSeller().getId().equals(sellerId)) {
            throw new DataNotFoundException("Product with ID " + id + " not found");
        }
        Category category = categoryRepository.findById(Math.toIntExact(createProductRequest.getCategoryId()))
                .orElseThrow(() -> new DataNotFoundException("Category not found"));
        productMapper.updateProductFromRequest(createProductRequest, product);
        product.setCategory(category);
        Product updated = productRepository.save(product);
        log.info("Updated product with ID {} by seller ID {}", id, sellerId);
        ProductDto productDto = productMapper.toProductDtoGetById(updated);
        log.info("Finished update of product with ID {} successfully", updated.getId());
        return productDto;
    }


    @Override
    public void delete(Integer id, HttpServletRequest request) {
        log.info("Starting deletion of product: {}", id);
        if (id <= 0) {
            throw new DataNotFoundException("ID must be positive");
        }
        var token = request.getHeader("Authorization").substring(7).trim();
        var userId = jwtService.extractUserId(token);
        var sellerId = userRepository.findById(userId).get().getSeller().getId();
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product with ID " + id + " not found"));
        if (!product.getSeller().getId().equals(sellerId)) {
            throw new DataNotFoundException("Product with ID " + id + " not found");
        }
        productRepository.deleteById(id);
        log.info("Deleted product with ID {} by seller ID {}", id, sellerId);
        log.info("Finished deletion of product with id: {}", id);
    }

    @Override
    public List<ProductDto> getProductsByCategory(Integer categoryId) {
        log.info("Starting retrieval of products by category: {}", categoryId);
        if (categoryId <= 0) {
            throw new DataNotFoundException("Category ID must be positive");
        }
        Category category = categoryRepository.findById(categoryId).orElseThrow(
                () -> new DataNotFoundException("Category not found with: " + categoryId)
        );
        List<Product> product = productRepository.findByCategory(category);
        List<ProductDto> productDtoList = reviewsMapper.getProductDtoList(product);
        productDtoList.stream()
                .map(productDto -> {
                    Product p = product.stream()
                            .filter(prd -> prd.getId().equals(productDto.getId()))
                            .findFirst()
                            .orElse(null);

                    if (p != null && p.getCategory() != null && p.getSeller() != null) {
                        productDto.setCategoryId(Long.valueOf(p.getCategory().getId()));
                        productDto.setSellerId(p.getSeller().getId());
                    } else {
                        productDto.setCategoryId(null);
                        productDto.setSellerId(null);
                    }
                    return productDto;
                })
                .toList();
        log.info("Finished retrieval of products by category: {}", categoryId);
        return productDtoList;
    }

    @Override
    public List<ProductDto> getProductsBySeller(Integer sellerId) {
        log.info("Starting retrieval of products by seller: {}", sellerId);
        if (sellerId <= 0) {
            throw new DataNotFoundException("Seller ID mustb be positive!");
        }
        List<Product> products = productRepository.findBySellerId(sellerId);
        List<ProductDto> productDtoList = reviewsMapper.getProductDtoList(products);
        log.info("Finished retrieval of products by seller: {}", sellerId);
        return productDtoList;

    }
}
