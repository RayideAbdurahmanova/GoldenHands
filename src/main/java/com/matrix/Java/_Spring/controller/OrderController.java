package com.matrix.Java._Spring.controller;

import com.matrix.Java._Spring.dto.CreateOrderRequest;
import com.matrix.Java._Spring.dto.OrderDto;
import com.matrix.Java._Spring.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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

    @GetMapping("/ByOrderId/{id}")
    public OrderDto getById(@PathVariable Integer id) {
       return orderService.getById(id);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public OrderDto create(@RequestBody CreateOrderRequest createOrderRequest) {
       return orderService.create(createOrderRequest);
    }

    @PutMapping("/update/{id}")
    public OrderDto update(@PathVariable Integer id,
                           @RequestBody CreateOrderRequest createOrderRequest) {
        return orderService.update(id,createOrderRequest);
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable Integer id) {
        orderService.delete(id);
    }
}
