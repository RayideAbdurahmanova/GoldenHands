package com.matrix.Java._Spring.controller;


import com.matrix.Java._Spring.dto.SellerDto;
import com.matrix.Java._Spring.service.SellerService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/seller")
public class SellerController {

    private final SellerService sellerService;

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<SellerDto> getSellers() {
        return sellerService.getSellers();
    }
}
