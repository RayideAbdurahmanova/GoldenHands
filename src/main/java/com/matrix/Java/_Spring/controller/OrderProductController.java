package com.matrix.Java._Spring.controller;


import com.matrix.Java._Spring.dto.CreateOrderProductRequest;
import com.matrix.Java._Spring.dto.OrderProductDto;
import com.matrix.Java._Spring.service.OrderProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/order_products")
public class OrderProductController {

    private final OrderProductService orderProductService;


//    @GetMapping("/OrderProductListWithOrderId/{orderId}")
//    public List<OrderProductDto> getOrderProductListWithOrderId(@PathVariable Integer orderId) {
//        return orderProductService.getOrderProductListWithOrderId(orderId);
//    }

    @GetMapping("/ByOrderProductId/{id}")
    public OrderProductDto getById(@PathVariable Integer id) {
       return   orderProductService.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderProductDto create(@RequestBody CreateOrderProductRequest createOrderProductRequest) {
      return orderProductService.create(createOrderProductRequest);
    }

    @PutMapping("/update/{id}")
    public OrderProductDto update(@PathVariable Integer id,
                                              @RequestBody CreateOrderProductRequest createOrderProductRequest) {
        return orderProductService.update(id,createOrderProductRequest);
    }

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer orderId,
                                   @RequestParam Integer orderProductId) {
        orderProductService.delete(orderId,orderProductId);
    }



}
