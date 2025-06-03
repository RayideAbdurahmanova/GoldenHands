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
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
        log.info("Finished retrieval {} address successfully", addressDto);
        return addressDto;
    }

    @Override
    public AddressDto getByCustomerId(Integer customerId) {
        log.info("Starting retrieval of customer with id {}", customerId);
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new DataNotFoundException("Address not found"));
        Address address = addressRepository.findByCustomer(customer);
        if (address == null) {
            throw new DataNotFoundException("Address not found");
        }
        AddressDto addressDto = addressMapper.toAddressDtoGetById(address);
        log.info("Finished retrieval {} address with ID: {} successfully", addressDto);
        return addressDto;
    }

    @Override
    public AddressDto create(CreateAddressRequest createAddressRequest, HttpServletRequest request) {
        log.info("Starting creating of address : {}", createAddressRequest);
        var token = request.getHeader("Authorization").substring(7).trim();
        var userId = jwtService.extractUserId(token);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException("User with ID " + userId + " not found"));
        Address address = addressMapper.toCreateAddressRequest(createAddressRequest);
        address.setUser(user);
        Address saved = addressRepository.save(address);
        AddressDto addressDto = addressMapper.toAddressDtoGetById(saved);
        log.info("Finished creation of address with id: {}", saved.getId());
        return addressDto;
    }

    @Override
    @Transactional
    public AddressDto update(Integer id, CreateAddressRequest createAddressRequest, HttpServletRequest request) {
        log.info("Starting  update of address with ID: {} ", id);
        var token = request.getHeader("Authorization").substring(7).trim();
        var userId = jwtService.extractUserId(token);
        Customer customer = customerRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException("Customer with ID " + userId + " not found"));

        Address existing = addressRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Address not found"));

        if (!existing.getCustomer().getCustomerId().equals(userId)) {
            throw new AccessDeniedException("Address does not belong to customer with ID " + userId);
        }

        addressMapper.updateAddressFromDto(createAddressRequest, existing);
        Address update = addressRepository.save(existing);
        log.info("Updated address with ID {} for customer ID {}", id, userId);
        AddressDto addressDto = addressMapper.toAddressDtoGetById(update);
        log.info("Finished update of address with ID {} successfully", existing.getId());
        return addressDto;

    }

    @Override
    @Transactional
    public void delete(Integer id, HttpServletRequest request) {
        log.info("Starting  deletion of address with ID : {} ", id);
        var token = request.getHeader("Authorization").substring(7).trim();
        var userId = jwtService.extractUserId(token);
        Customer customer = customerRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException("Customer with ID " + userId + " not found"));

        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Address not found"));

        if (!address.getCustomer().getCustomerId().equals(userId)) {
            throw new AccessDeniedException("Address does not belong to customer with ID " + userId);
        }
        addressRepository.deleteById(id);
        log.info("Finished deletion of product with id: {}", id);
    }
}
