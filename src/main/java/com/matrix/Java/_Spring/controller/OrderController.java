package com.matrix.Java._Spring.controller;

import com.matrix.Java._Spring.dto.CreateOrderRequest;
import com.matrix.Java._Spring.dto.OrderDto;
import com.matrix.Java._Spring.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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


    @GetMapping("/customer/{customerId}")
    public List<OrderDto> getListByCustomerId(@PathVariable Integer customerId) {
        return orderService.getListByCustomerId(customerId);
    }

    @GetMapping("/{id}")
    public OrderDto getById(@PathVariable Integer id) {
        return orderService.getById(id);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('USER')")
    public OrderDto create(@RequestBody CreateOrderRequest createOrderRequest,
                           HttpServletRequest request) {
        return orderService.create(createOrderRequest,request);
    }

    @PutMapping("/{id}")
    public OrderDto update(@PathVariable Integer id,
                           @RequestBody CreateOrderRequest createOrderRequest,
                           HttpServletRequest request) {
        return orderService.update(id, createOrderRequest, request);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id,
                       HttpServletRequest request) {
        orderService.delete(id, request);
        // have to look
    }
}
