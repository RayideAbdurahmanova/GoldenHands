package com.matrix.Java._Spring.controller;


import com.matrix.Java._Spring.dto.CreateProductRequest;
import com.matrix.Java._Spring.dto.ProductDto;
import com.matrix.Java._Spring.exceptions.DataNotFoundException;
import com.matrix.Java._Spring.model.entity.Category;
import com.matrix.Java._Spring.model.entity.Product;
import com.matrix.Java._Spring.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    @GetMapping("/ProductList")
    public List<ProductDto> getList() {
        return productService.getList();
    }

    @GetMapping("/ByProductId/{id}")
    public ProductDto getById(@PathVariable Integer id) {
        return productService.getById(id);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public ProductDto create(@RequestBody CreateProductRequest createProductRequest) {
        return productService.create(createProductRequest);
    }

    @PutMapping("/update/{id}")
    public ProductDto update(@PathVariable Integer id,
                                    @RequestBody CreateProductRequest createProductRequest) {
        return productService.update(id,createProductRequest);
    }

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        productService.delete(id);
    }

    @GetMapping("ByCategory/{categoryId}")
    public List<ProductDto> getProductsByCategory(@PathVariable Integer categoryId) {
        return productService.getProductsByCategory(categoryId);
    }
}
