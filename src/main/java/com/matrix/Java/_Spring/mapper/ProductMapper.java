package com.matrix.Java._Spring.mapper;

import com.matrix.Java._Spring.dto.CreateProductRequest;
import com.matrix.Java._Spring.dto.ProductDto;
import com.matrix.Java._Spring.model.entity.Product;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel="spring")
public interface ProductMapper {

    List<ProductDto> getProductDtoList(List<Product> products);

    ProductDto toProductDtoGetById(Product product);

    Product toCreateProductRequest(CreateProductRequest createProductRequest);
}
