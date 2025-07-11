package com.matrix.Java._Spring.mapper;

import com.matrix.Java._Spring.dto.BasketDto;
import com.matrix.Java._Spring.model.entity.Basket;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel="spring")
public interface BasketMapper {

    @Mapping(source = "items", target = "items")
    BasketDto toDto(Basket basket);
}
