package com.matrix.Java._Spring.service;

import com.matrix.Java._Spring.dto.BasketDto;
import com.matrix.Java._Spring.dto.BasketRequest;
import jakarta.servlet.http.HttpServletRequest;

public interface BasketService {
    void addBasket(HttpServletRequest req, BasketRequest basketRequest);

    void delete(HttpServletRequest req, Integer productId);

    BasketDto getBasket(HttpServletRequest req);
}
