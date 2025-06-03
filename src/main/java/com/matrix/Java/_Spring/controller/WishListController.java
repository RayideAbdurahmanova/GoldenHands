package com.matrix.Java._Spring.controller;


import com.matrix.Java._Spring.dto.CreateWishListRequest;
import com.matrix.Java._Spring.dto.WishListDto;
import com.matrix.Java._Spring.service.WishListService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/wishlist")
public class WishListController {

    private final WishListService wishListService;


    @GetMapping("/customer/{customerId}")
    public List<WishListDto> getListByCustomerId(@PathVariable Integer customerId) {
        return wishListService.getListByCustomerId(customerId);
    }


    @PostMapping()
    public WishListDto create(@RequestBody CreateWishListRequest createWishListRequest,
                              HttpServletRequest request) {
        return wishListService.create(createWishListRequest, request);
    }

    @PutMapping("/{id}")
    public WishListDto update(@PathVariable Integer id,
                              @RequestBody CreateWishListRequest createWishListRequest,
                              HttpServletRequest request) {
        return wishListService.update(id, createWishListRequest, request);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id,
                       HttpServletRequest request) {
        wishListService.delete(id, request);
    }
}


