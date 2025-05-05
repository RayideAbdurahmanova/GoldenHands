package com.matrix.Java._Spring.service;

import com.matrix.Java._Spring.dto.CreateWishListRequest;
import com.matrix.Java._Spring.dto.CustomerDto;
import com.matrix.Java._Spring.dto.WishListDto;
import com.matrix.Java._Spring.model.entity.Customer;
import jakarta.persistence.criteria.CriteriaBuilder;

import java.util.List;

public interface WishListService {

    List<WishListDto> getListByCustomerId(Integer customerId);

//    WishListDto getById(Integer id);

    WishListDto create(CreateWishListRequest createWishListRequest);

    WishListDto update(Integer id, CreateWishListRequest createWishListRequest);

    void delete(Customer customerId, Integer id);
}
