package com.matrix.Java._Spring.controller;


import com.matrix.Java._Spring.dto.BalanceDto;
import com.matrix.Java._Spring.service.BalanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/balance")
public class BalanceController {

    private final BalanceService balanceService;

    @GetMapping
    public BalanceDto getBalanceByUserId(Integer userId) {
        return balanceService.getBalanceByUserId(userId);
    }
}
