package com.matrix.Java._Spring.controller;


import com.matrix.Java._Spring.dto.CreateProductRequest;
import com.matrix.Java._Spring.dto.ProductDto;
import com.matrix.Java._Spring.jwt.JwtService;
import com.matrix.Java._Spring.jwt.impl.JwtServiceImpl;
import com.matrix.Java._Spring.service.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;
    private final JwtService jwtService;
    private final JwtServiceImpl jwtServiceImpl;

    @GetMapping()
    public List<ProductDto> getList() {
        return productService.getList();
    }

    @GetMapping("/{id}")
    public ProductDto getById(@PathVariable Integer id) {
        return productService.getById(id);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('SELLER')")
    public ProductDto create(@RequestBody @Valid CreateProductRequest createProductRequest,
                             HttpServletRequest request) {
        return productService.create(createProductRequest, request);
    }

    @PutMapping("/{id}")
    public ProductDto update(@PathVariable Integer id,
                             @RequestBody CreateProductRequest createProductRequest,
                             HttpServletRequest request) {
        return productService.update(id, createProductRequest, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id,
                       HttpServletRequest request) {
        productService.delete(id, request);
    }

    @GetMapping("/category/{categoryId}")
    public List<ProductDto> getProductsByCategory(@PathVariable Integer categoryId) {
        return productService.getProductsByCategory(categoryId);
    }
}
