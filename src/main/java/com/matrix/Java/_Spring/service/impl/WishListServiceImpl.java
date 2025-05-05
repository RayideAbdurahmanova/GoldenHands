package com.matrix.Java._Spring.service.impl;


import com.matrix.Java._Spring.dto.CreateWishListRequest;
import com.matrix.Java._Spring.dto.WishListDto;
import com.matrix.Java._Spring.exceptions.DataNotFoundException;
import com.matrix.Java._Spring.mapper.WishListMapper;
import com.matrix.Java._Spring.mapper.WishListMapperImpl;
import com.matrix.Java._Spring.model.entity.Customer;
import com.matrix.Java._Spring.model.entity.WishList;
import com.matrix.Java._Spring.repository.CustomerRepository;
import com.matrix.Java._Spring.repository.WishListRepository;
import com.matrix.Java._Spring.service.WishListService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class WishListServiceImpl implements WishListService {


    private final CustomerRepository customerRepository;
    private final WishListRepository wishListRepository;
    private final WishListMapper wishListMapper;

    @Override
    public List<WishListDto> getListByCustomerId(Integer customerId) {
        Customer customer=customerRepository.findById(customerId)
                .orElseThrow(()->new DataNotFoundException("Data not found"));

        List<WishList> wishLists=wishListRepository.findAllByCustomer(customer);
        return wishListMapper.getWishListDtoList(wishLists);
    }

//    @Override
//    public WishListDto getWishListById(Integer id) {
//        return null;
//    }

    @Override
    public WishListDto create(CreateWishListRequest createWishListRequest) {
        return null;
    }

    @Override
    public WishListDto update(Integer id, CreateWishListRequest createWishListRequest) {
        return null;
    }

    @Override
    public void delete(Customer customerId, Integer id) {

    }
}
