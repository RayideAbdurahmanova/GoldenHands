package com.matrix.Java._Spring.controller;


import com.matrix.Java._Spring.dto.CreateCustomerRequest;
import com.matrix.Java._Spring.dto.CustomerDto;
import com.matrix.Java._Spring.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping("/CustomerList")
    public List<CustomerDto> getCustomerList() {
        return customerService.getList();
    }

    @GetMapping("/ByCustomerId/{id}")
    public CustomerDto getCustomerById(@PathVariable Integer id) {
        return customerService.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerDto createCustomer(@RequestBody CreateCustomerRequest createCustomerRequest) {
        return customerService.create(createCustomerRequest);
    }

    @PutMapping("/update/{id}")
    public CustomerDto updateCustomer(@PathVariable Integer id,
                                      @RequestBody CreateCustomerRequest createCustomerRequest) {
        return customerService.update(id,createCustomerRequest);
    }


    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCustomer(@PathVariable Integer id) {
        customerService.delete(id);
    }

}
