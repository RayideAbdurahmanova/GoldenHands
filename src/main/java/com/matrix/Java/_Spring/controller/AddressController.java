package com.matrix.Java._Spring.controller;


import com.matrix.Java._Spring.dto.AddressDto;
import com.matrix.Java._Spring.exceptions.DataNotFoundException;
import com.matrix.Java._Spring.model.entity.Address;
import com.matrix.Java._Spring.model.entity.Customer;
import com.matrix.Java._Spring.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/addresses")
public class AddressController {

    private final AddressService addressService;


    @GetMapping("/List")
    public List<AddressDto> getList() {
       return addressService.getList();
    }

    @GetMapping("/ById/{id}")
    public AddressDto getById(@PathVariable Integer id) {
       return addressService.getById(id);
    }


    @GetMapping("ByCustomerId/{customerId}")
    public AddressDto getByCustomerId(@PathVariable Integer customerId) {
        return addressService.getByCustomerId(customerId);
    }

}
