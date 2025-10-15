package com.matrix.Java._Spring.mapper;

import com.matrix.Java._Spring.dto.CreateProductRequest;
import com.matrix.Java._Spring.dto.ProductDto;
import com.matrix.Java._Spring.model.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel="spring")
public interface ProductMapper {

    @Mapping(target = "categoryId", source = "category.id")
    @Mapping(target = "sellerId", source = "seller.id")
    ProductDto toProductDtoGetById(Product product);

    Product toCreateProductRequest(CreateProductRequest createProductRequest);

    void updateProductFromRequest(CreateProductRequest createProductRequest, @MappingTarget Product product);
}
