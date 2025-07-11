package com.matrix.Java._Spring.mapper;

import com.matrix.Java._Spring.dto.CreateWishListRequest;
import com.matrix.Java._Spring.dto.WishListDto;
import com.matrix.Java._Spring.dto.WishListProduct;
import com.matrix.Java._Spring.model.entity.Reviews;
import com.matrix.Java._Spring.model.entity.WishList;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface WishListMapper {

    @Mapping(target = "userId", source = "user.id")
    WishListDto toWishListDtoGetById(WishList wishLists);

    WishList toCreateWishListRequest(CreateWishListRequest createWishListRequest);
}
