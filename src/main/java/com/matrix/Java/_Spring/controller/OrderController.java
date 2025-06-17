package com.matrix.Java._Spring.controller;

import com.matrix.Java._Spring.dto.CreateOrderRequest;
import com.matrix.Java._Spring.dto.OrderDto;
import com.matrix.Java._Spring.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/customer")
    @PreAuthorize("hasAuthority('USER')")
    public List<OrderDto> getListByCustomerId() {
        return orderService.getListByCustomerId();
    }

    @GetMapping("/customer-order/{id}")
    @PreAuthorize("hasAuthority('USER')")
    public OrderDto getCustomerOrder(@PathVariable Integer id) {
        return orderService.getCustomerOrder(id);
    }


    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public OrderDto getById(@PathVariable Integer id) {
        return orderService.getById(id);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<OrderDto> getAll() {
        return orderService.getAll();
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('USER')")
    public OrderDto create(@Valid @RequestBody CreateOrderRequest createOrderRequest,
                           HttpServletRequest request) {
        return orderService.create(createOrderRequest,request);
    }

    @PutMapping("/{id}")
    public OrderDto update(@PathVariable Integer id,
                           @RequestBody CreateOrderRequest createOrderRequest,
                           HttpServletRequest request) {
        return orderService.update(id, createOrderRequest, request);
    }
    //have to look
}
