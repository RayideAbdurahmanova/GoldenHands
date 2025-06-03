package com.matrix.Java._Spring.dto;

import com.matrix.Java._Spring.model.entity.Customer;
import com.matrix.Java._Spring.model.entity.Seller;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class BalanceDto {
    private BigDecimal amount;
    private Customer customer;
    private Seller seller;
    private LocalDateTime lastUpdated;
    private LocalDateTime createdAt;
}
