package com.matrix.Java._Spring.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class BasketDto {

    Long id;
    BigDecimal totalPrice;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    List<BasketItemDto> items;
}
