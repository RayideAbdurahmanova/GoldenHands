package com.matrix.Java._Spring.controller;


import com.matrix.Java._Spring.dto.AddressDto;
import com.matrix.Java._Spring.dto.CreateAddressRequest;
import com.matrix.Java._Spring.service.AddressService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/addresses")
public class AddressController {

    private final AddressService addressService;

    @GetMapping()
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<AddressDto> getList() {
        return addressService.getList();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public AddressDto getById(@PathVariable Integer id) {
        return addressService.getById(id);
    }

    @GetMapping("my-address")
    @PreAuthorize("hasAnyAuthority('ADMIN','SELLER','USER')")
    public AddressDto getByCustomerId() {
        return addressService.getByCustomerId();
    }

    @PostMapping()
    @PreAuthorize("hasAnyAuthority('ADMIN','SELLER','USER')")
    public void create(@RequestBody @Valid CreateAddressRequest createAddressRequest,
                       HttpServletRequest request) {
        addressService.create(createAddressRequest, request);
    }


    @DeleteMapping()
    @PreAuthorize("hasAnyAuthority('ADMIN','SELLER','USER')")
    public void delete(HttpServletRequest request) {
        addressService.delete(request);
    }

}
