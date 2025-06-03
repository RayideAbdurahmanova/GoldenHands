package com.matrix.Java._Spring.controller;


import com.matrix.Java._Spring.dto.AddressDto;
import com.matrix.Java._Spring.dto.CreateAddressRequest;
import com.matrix.Java._Spring.service.AddressService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/addresses")
public class AddressController {

    private final AddressService addressService;

    @GetMapping()
    public List<AddressDto> getList() {
       return addressService.getList();
    }

    @GetMapping("/{id}")
    public AddressDto getById(@PathVariable Integer id) {
       return addressService.getById(id);
    }

    @GetMapping("customer/{customerId}")
    public AddressDto getByCustomerId(@PathVariable Integer customerId) {
        return addressService.getByCustomerId(customerId);
    }

    @PostMapping()
    public AddressDto create(@RequestBody @Valid CreateAddressRequest createAddressRequest,
                             HttpServletRequest request) {
        return addressService.create(createAddressRequest,request);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id,
                       HttpServletRequest request) {
        addressService.delete(id,request);
    }

}
