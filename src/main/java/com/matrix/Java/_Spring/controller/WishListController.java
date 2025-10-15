package com.matrix.Java._Spring.controller;


import com.matrix.Java._Spring.dto.CreateWishListRequest;
import com.matrix.Java._Spring.dto.WishListDto;
import com.matrix.Java._Spring.service.WishListService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/wishlist")
@PreAuthorize("hasAuthority('USER')")
@Validated
public class WishListController {

    private final WishListService wishListService;

    @GetMapping("/user")
    @ResponseStatus(HttpStatus.OK)
    public WishListDto getListByUserId() {
        return wishListService.getListByUserId();
    }


    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public WishListDto create(@Valid @RequestBody CreateWishListRequest createWishListRequest,
                              HttpServletRequest request) {
        return wishListService.create(createWishListRequest, request);
    }


    @DeleteMapping()
    @PreAuthorize("hasAuthority('USER')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(HttpServletRequest request) {
        wishListService.delete(request);
    }

    @DeleteMapping("/product/{productId}")
    @PreAuthorize("hasAuthority('USER')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeProduct(@PathVariable Integer productId,
                              HttpServletRequest request) {
        wishListService.removeProduct(productId, request);
    }
}


