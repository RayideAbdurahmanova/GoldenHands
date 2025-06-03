package com.matrix.Java._Spring.controller;


import com.matrix.Java._Spring.dto.CreateOrderProductRequest;
import com.matrix.Java._Spring.dto.OrderProductDto;
import com.matrix.Java._Spring.service.OrderProductService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/order_products")
public class OrderProductController {

    private final OrderProductService orderProductService;

    @GetMapping("/orderProducts-withOrderId/{orderId}")
    public List<OrderProductDto> getOrderProductListWithOrderId(@PathVariable Integer orderId,
                                                                HttpServletRequest request) {
        return orderProductService.getWithOrderId(orderId, request);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('USER')")
    public OrderProductDto getById(@PathVariable Integer id,
                                   HttpServletRequest request) {
        return orderProductService.getById(id, request);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderProductDto create(@RequestBody CreateOrderProductRequest createOrderProductRequest, HttpServletRequest request) {
        return orderProductService.create(createOrderProductRequest, request);
    }

    @PutMapping("/{id}")
    public OrderProductDto update(@PathVariable Integer id,
                                  @RequestBody CreateOrderProductRequest createOrderProductRequest,
                                  HttpServletRequest request) {
        return orderProductService.update(id, createOrderProductRequest, request);
    }

    @DeleteMapping("/{orderId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer orderId,
                       @RequestParam Integer orderProductId,
                       HttpServletRequest request) {
        orderProductService.delete(orderId, orderProductId, request);
    }


}
