package com.matrix.Java._Spring.controller;

import com.matrix.Java._Spring.dto.BasketDto;
import com.matrix.Java._Spring.dto.BasketRequest;
import com.matrix.Java._Spring.model.entity.Basket;
import com.matrix.Java._Spring.service.BasketService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/basket")
public class BasketController {
    private final BasketService basketService;

    @PostMapping
    @PreAuthorize("hasAuthority('USER')")
    @ResponseStatus(HttpStatus.CREATED)
    public void addBasket(HttpServletRequest req, @RequestBody @Valid BasketRequest basketRequest) {
        basketService.addBasket(req, basketRequest);
    }

    @DeleteMapping("/{productId}")
    @PreAuthorize("hasAuthority('USER')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(HttpServletRequest req, @PathVariable Integer productId) {
        basketService.delete(req, productId);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('USER')")
    @ResponseStatus(HttpStatus.OK)
    public BasketDto getBasket(HttpServletRequest req) {
      return basketService.getBasket(req);
    }
}
