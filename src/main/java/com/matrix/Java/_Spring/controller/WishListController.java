package com.matrix.Java._Spring.controller;


import com.matrix.Java._Spring.dto.CreateWishListRequest;
import com.matrix.Java._Spring.dto.WishListDto;
import com.matrix.Java._Spring.service.WishListService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/wishlist")
public class WishListController {

    private final WishListService wishListService;


    @GetMapping("/user")
    @PreAuthorize("hasAuthority('USER')")
    public List<WishListDto> getListByUserId() {
        return wishListService.getListByUserId();
    }


    @PostMapping()
    @PreAuthorize("hasAuthority('USER')")
    public WishListDto create(@Valid @RequestBody CreateWishListRequest createWishListRequest,
                              HttpServletRequest request) {
        return wishListService.create(createWishListRequest, request);
    }


    @DeleteMapping()
    @PreAuthorize("hasAuthority('USER')")
    public void delete(HttpServletRequest request) {
        wishListService.delete(request);
    }

    @DeleteMapping("/product/{productId}")
    @PreAuthorize("hasAuthority('USER')")
    public void removeProduct(@PathVariable Integer productId,
                              HttpServletRequest request) {
        wishListService.removeProduct(productId, request);
    }
}


