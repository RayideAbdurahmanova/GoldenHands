package com.matrix.Java._Spring.controller;


import com.matrix.Java._Spring.dto.CreateProductRequest;
import com.matrix.Java._Spring.dto.ProductDto;
import com.matrix.Java._Spring.service.ProductService;
import jakarta.persistence.criteria.CriteriaBuilder;
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

    @GetMapping()
    @PreAuthorize("hasAnyAuthority('ADMIN','USER','SELLER')")
    @ResponseStatus(HttpStatus.OK)
    public List<ProductDto> getList() {
        return productService.getList();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','USER','SELLER')")
    @ResponseStatus(HttpStatus.OK)
    public ProductDto getById(@PathVariable Integer id) {
        return productService.getById(id);
    }

    @PostMapping()
    @PreAuthorize("hasAuthority('SELLER')")
    @ResponseStatus(HttpStatus.CREATED)
    public ProductDto create(@RequestBody @Valid CreateProductRequest createProductRequest,
                             HttpServletRequest request) {
        return productService.create(createProductRequest, request);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('SELLER')")
    public ProductDto update(@PathVariable Integer id,
                             @RequestBody @Valid CreateProductRequest createProductRequest,
                             HttpServletRequest request) {
        return productService.update(id, createProductRequest, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyAuthority('SELLER','ADMIN')")
    public void delete(@PathVariable Integer id,
                       HttpServletRequest request) {
        productService.delete(id, request);
    }

    @GetMapping("/category/{categoryId}")
    @PreAuthorize("hasAnyAuthority('ADMIN','USER','SELLER')")
    @ResponseStatus(HttpStatus.OK)
    public List<ProductDto> getProductsByCategory(@PathVariable Integer categoryId) {
        return productService.getProductsByCategory(categoryId);
    }

    @GetMapping("/seller/{sellerId}")
    @PreAuthorize("hasAnyAuthority('ADMIN','USER','SELLER')")
    @ResponseStatus(HttpStatus.OK)
    public List<ProductDto> getProductsBySeller(@PathVariable Integer sellerId){
    return productService.getProductsBySeller(sellerId);
    }

}
