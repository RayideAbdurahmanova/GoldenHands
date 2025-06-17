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
        var email = SecurityContextHolder.getContext().getAuthentication().getName();
        var user = userRepository.findByUsername(email).orElseThrow(
                () -> new AccessDeniedException("User not found")
        );

        log.info("Starting retrieval of customer with id {}", user.getCustomer().getCustomerId());
        Customer customer = customerRepository.findById(user.getCustomer().getCustomerId())
                .orElseThrow(() -> new DataNotFoundException("Address not found"));
        Address address = addressRepository.findByCustomer(customer);
        if (address == null) {
            throw new DataNotFoundException("Address not found");
        }
        AddressDto addressDto = addressMapper.toAddressDtoGetById(address);
        log.info("Finished retrieval {} address with Customer ID successfully", addressDto);
        return addressDto;
    }

    @Override
    public void create(CreateAddressRequest createAddressRequest, HttpServletRequest request) {
        log.info("Starting creating of address : {}", createAddressRequest);
        var token = request.getHeader("Authorization").substring(7).trim();
        var userId = jwtService.extractUserId(token);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException("User with ID " + userId + " not found"));
        Address existingAddress = user.getAddress();
        Address saved;
        if (existingAddress == null) {
            log.info("No existing address found for user ID: {}", userId);
            Address address = addressMapper.toCreateAddressRequest(createAddressRequest);
            address.setUser(user);
            address.setCustomer(user.getCustomer());
            var customer = user.getCustomer();
            customer.setAddressEntity(address);
            user.setAddress(address);
            saved = addressRepository.save(address);
        } else {
            log.info("Existing address found for user ID: {}", userId);
            addressMapper.updateAddressFromRequest(createAddressRequest, existingAddress);
            saved = addressRepository.save(existingAddress);
            log.info("Updating existing address : {}", saved);
        }
        log.info("Finishing creating of address with ID : {}", saved.getId());

    }


    @Override
    @Transactional
    public void delete(HttpServletRequest request) {
        var token = request.getHeader("Authorization").substring(7).trim();
        var userId = jwtService.extractUserId(token);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException("User with ID " + userId + " not found"));
        Address address = user.getAddress();

        if (address == null) {
            throw new DataNotFoundException("Address not found");
        }
        log.info("Attempting to delete address with ID: {}", address.getId());
        user.setAddress(null);

        if(address.getCustomer() != null) {
            address.getCustomer().setAddressEntity(null);
            customerRepository.save(address.getCustomer());
            log.info("Cleared Customer.addressEntity for customer ID: {}", address.getCustomer().getCustomerId());
        }
        if(address.getSeller() != null) {
            address.setSeller(null);
            addressRepository.save(address);
        }

    }
}
