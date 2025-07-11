package com.matrix.Java._Spring.mapper;


import com.matrix.Java._Spring.dto.CreateReviewsRequest;
import com.matrix.Java._Spring.dto.ProductDto;
import com.matrix.Java._Spring.dto.ReviewsDto;
import com.matrix.Java._Spring.model.entity.Product;
import com.matrix.Java._Spring.model.entity.Reviews;
import jdk.jfr.MemoryAddress;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.security.core.parameters.P;

import java.util.List;

@Mapper(componentModel="spring")
public interface ReviewsMapper {


    List<ReviewsDto> getReviewsDtoList(List<Reviews> reviews);

    @Mapping(target = "productId", source = "product.id")
    ReviewsDto toReviewsDtoGetById(Reviews reviews);

    @Mapping(target = "reviews", source = "reviews")
    List<ProductDto> getProductDtoList(List<Product> products);

    @Mapping(target = "sellerId", source = "seller.id")
    @Mapping(target = "categoryId", source = "category.id")
    ProductDto toProductDtoGetById(Product product);


    @Mapping(target = "product.id", source = "productId")
    Reviews toCreateReviewsRequest(CreateReviewsRequest createReviewsRequest);
}
