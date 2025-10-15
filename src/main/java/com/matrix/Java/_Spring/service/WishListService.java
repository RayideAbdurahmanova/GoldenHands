package com.matrix.Java._Spring.service;

import com.matrix.Java._Spring.dto.CreateWishListRequest;
import com.matrix.Java._Spring.dto.WishListDto;
import jakarta.servlet.http.HttpServletRequest;

public interface WishListService {

    WishListDto getListByUserId();

    WishListDto create(CreateWishListRequest createWishListRequest, HttpServletRequest request);

    void delete(HttpServletRequest request);

    void removeProduct(Integer productId,HttpServletRequest request);
}
