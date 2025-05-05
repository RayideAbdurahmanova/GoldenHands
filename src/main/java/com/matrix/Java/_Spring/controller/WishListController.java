package com.matrix.Java._Spring.controller;


import com.matrix.Java._Spring.dto.WishListDto;
import com.matrix.Java._Spring.exceptions.DataNotFoundException;
import com.matrix.Java._Spring.model.entity.Customer;
import com.matrix.Java._Spring.model.entity.WishList;
import com.matrix.Java._Spring.service.WishListService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/wishlist")
public class WishListController {

    private final WishListService wishListService;


    @GetMapping("/ByCustomerId/{customerId}")
    public List<WishListDto> getListByCustomerId(@PathVariable Integer customerId) {
       return wishListService.getListByCustomerId(customerId);
    }
}
