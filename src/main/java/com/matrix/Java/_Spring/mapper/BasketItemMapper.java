package com.matrix.Java._Spring.mapper;

import com.matrix.Java._Spring.dto.BasketItemDto;
import com.matrix.Java._Spring.model.entity.BasketItem;
import org.mapstruct.Mapping;

public interface BasketItemMapper {

    @Mapping(source = "product.id", target = "productId")
    BasketItemDto basketItemDto(BasketItem basketItem);
}
