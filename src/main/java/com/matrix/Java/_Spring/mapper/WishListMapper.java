package com.matrix.Java._Spring.mapper;

import com.matrix.Java._Spring.dto.CreateWishListRequest;
import com.matrix.Java._Spring.dto.WishListDto;
import com.matrix.Java._Spring.model.entity.WishList;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel="spring")
public interface WishListMapper {

    List<WishListDto> getWishListDtoList(List<WishList> wishLists);

    WishListDto toWishListDtoGetById(WishList wishLists);

    WishList toCreateWishListRequest(CreateWishListRequest createWishListRequest);
}
