package com.matrix.Java._Spring.service;

import com.matrix.Java._Spring.dto.CreateWishListRequest;
import com.matrix.Java._Spring.dto.WishListDto;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface WishListService {

    List<WishListDto> getListByCustomerId(Integer customerId);

//    WishListDto getById(Integer id);

    WishListDto create(CreateWishListRequest createWishListRequest, HttpServletRequest request);

    WishListDto update(Integer id, CreateWishListRequest createWishListRequest,HttpServletRequest request);

    void delete(Integer id,HttpServletRequest request);
}
