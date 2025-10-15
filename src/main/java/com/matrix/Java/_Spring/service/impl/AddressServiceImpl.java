package com.matrix.Java._Spring.service.impl;

import com.matrix.Java._Spring.dto.AddressDto;
import com.matrix.Java._Spring.dto.CreateAddressRequest;
import com.matrix.Java._Spring.exceptions.AccessDeniedException;
import com.matrix.Java._Spring.exceptions.DataNotFoundException;
import com.matrix.Java._Spring.jwt.JwtService;
import com.matrix.Java._Spring.mapper.AddressMapper;
import com.matrix.Java._Spring.model.entity.Address;
import com.matrix.Java._Spring.model.entity.Customer;
import com.matrix.Java._Spring.model.entity.User;
import com.matrix.Java._Spring.repository.AddressRepository;
import com.matrix.Java._Spring.repository.CustomerRepository;
import com.matrix.Java._Spring.repository.UserRepository;
import com.matrix.Java._Spring.service.AddressService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;
    private final CustomerRepository customerRepository;
    private final JwtService jwtService;
    private final UserRepository userRepository;


    @Override
    public List<AddressDto> getList() {
        log.info("Starting retrieval of all addresses");
        List<Address> addresses = addressRepository.findAll();
        List<AddressDto> addressDtos = addressMapper.getAddressDtoList(addresses);
        log.info("Finished retrieval {} addresses successfully", addresses.size());
        return addressDtos;
    }

    @Override
    public AddressDto getById(Integer id) {
        log.info("Starting retrieval of address with id {}", id);
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Address not found"));
        AddressDto addressDto = addressMapper.toAddressDtoGetById(address);
        log.info("Finished retrieval {} with address ID successfully", addressDto);
        return addressDto;
    }

    @Override
    public AddressDto getByCustomerId() {
        log.info("Starting retrieval of address by customer id");
        var email = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("Authenticated user email: {}", email);
        var user = userRepository.findByUsername(email).orElseThrow(
                () -> new AccessDeniedException("User not found")
        );
        if (user.getAddress()== null) {
            log.error("Address ID is null for user ID: {}", user.getId());
            throw new DataNotFoundException("Address ID is null for USER:"+user.getId());
        }
        log.info("User found: ID={}, Address ID from DB: {}", user.getId(), user.getAddress().getId());

        Address address = addressRepository.findById(user.getAddress().getId())
                .orElseThrow(() -> new DataNotFoundException("Address not found for ID: " + user.getAddress().getId()));
        log.info("Address found: ID={}", address.getId());
        AddressDto addressDto = addressMapper.toAddressDtoGetById(address);
        log.info("Finished retrieval of address with ID {} for user successfully", addressDto.getId());
        return addressDto;
    }

    @Override
    @Transactional
    public void create(CreateAddressRequest createAddressRequest, HttpServletRequest request) {
        log.info("Starting creating of address : {}", createAddressRequest);
        var token = request.getHeader("Authorization").substring(7).trim();
        var userId = jwtService.extractUserId(token);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException("User with ID " + userId + " not found"));

        Customer customer = customerRepository.findById(user.getCustomer().getCustomerId())
                .orElseThrow(() -> new DataNotFoundException("Customer not found"));
        Address existingAddress = addressRepository.findByUserId(userId).orElse(null);
        Address saved;
        if (existingAddress == null) {
            log.info("No existing address found for user ID: {}", userId);
            Address address = addressMapper.toCreateAddressRequest(createAddressRequest);
            address.setUser(user);
            address.setCustomer(customer);
            user.setAddress(address);
            customer.setAddressEntity(address);
            addressRepository.save(address);
            userRepository.save(user);
            customerRepository.save(customer);
        } else {
            log.info("Existing address found for user ID: {}", userId);
            var address = addressMapper.updateAddressFromRequest(createAddressRequest, existingAddress);
            customer.setAddressEntity(address);
            saved = addressRepository.save(existingAddress);
            log.info("Updating existing address : {}", saved);
        }
    }
}
