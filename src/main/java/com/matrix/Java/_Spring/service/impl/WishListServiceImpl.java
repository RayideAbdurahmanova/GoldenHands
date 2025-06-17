package com.matrix.Java._Spring.service.impl;


import com.matrix.Java._Spring.dto.CreateWishListRequest;
import com.matrix.Java._Spring.dto.WishListDto;
import com.matrix.Java._Spring.exceptions.DataExistException;
import com.matrix.Java._Spring.exceptions.DataNotFoundException;
import com.matrix.Java._Spring.jwt.JwtService;
import com.matrix.Java._Spring.mapper.WishListMapper;
import com.matrix.Java._Spring.model.entity.Product;
import com.matrix.Java._Spring.model.entity.User;
import com.matrix.Java._Spring.model.entity.WishList;
import com.matrix.Java._Spring.repository.CustomerRepository;
import com.matrix.Java._Spring.repository.ProductRepository;
import com.matrix.Java._Spring.repository.UserRepository;
import com.matrix.Java._Spring.repository.WishListRepository;
import com.matrix.Java._Spring.service.WishListService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class WishListServiceImpl implements WishListService {


    private final CustomerRepository customerRepository;
    private final WishListRepository wishListRepository;
    private final WishListMapper wishListMapper;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Override
    public List<WishListDto> getListByUserId() {
        var email = SecurityContextHolder.getContext().getAuthentication().getName();
        var user = userRepository.findByUsername(email)
                .orElseThrow();

        List<WishList> wishLists = wishListRepository.findAllByUser(user);
        if (wishLists.isEmpty()) {
            log.info("No wishlists found for user ID: {}", user.getId());
            return List.of();
        }
        WishList wishList = wishLists.get(0);
        List<WishListDto> wishListDtos = wishList.getProducts() != null && !wishList.getProducts().isEmpty()
                ? wishList.getProducts().stream()
                .map(product -> {
                    WishListDto wishListDto = new WishListDto();
                    wishListDto.setId(wishList.getId());
                    wishListDto.setUserId(user.getId());
                    wishListDto.setProductId(product.getId());
                    return wishListDto;
                })
                .collect(Collectors.toList())
                : List.of();

        log.info("Finished retrieval {} wish lists", wishListDtos.size());
        return wishListDtos;
    }


    @Override
    public WishListDto create(CreateWishListRequest createWishListRequest, HttpServletRequest request) {
        log.info("Starting creating of wishlist: {}", createWishListRequest);

        if (createWishListRequest.getProductId() == null) {
            throw new IllegalArgumentException("Product ID cannot be null");
        }

        var token = request.getHeader("Authorization").substring(7).trim();
        var userId = jwtService.extractUserId(token);
        User user = userRepository.findById(userId).orElseThrow(
                () -> new DataNotFoundException("Customer not found with id: " + userId)
        );
        var product = productRepository.findById(Math.toIntExact(createWishListRequest.getProductId()))
                .orElseThrow(() -> new DataNotFoundException("Product Not Found With ID:" + createWishListRequest.getProductId()));

        WishList wishList;
        Optional<WishList> existingWishList = wishListRepository.findByUser(user);
        if (existingWishList.isPresent()) {
            wishList = existingWishList.get();
            if (wishList.getProducts().contains(product)) {
                throw new DataExistException("Product with ID: " + product.getId() + " already exists");
            }
            wishList.getProducts().add(product);
        } else {
            wishList = wishListMapper.toCreateWishListRequest(createWishListRequest);
            wishList.setUser(user);
            wishList.setProducts(new ArrayList<>());
            wishList.getProducts().add(product);
        }

        WishList savedWishList = wishListRepository.save(wishList);
        log.info("Created wishlist with ID {} for customer ID {}", savedWishList.getId(), userId);
        WishListDto wishListDto = wishListMapper.toWishListDtoGetById(savedWishList);
        wishListDto.setId(wishList.getId());
        wishListDto.setUserId(wishList.getUser() != null ? wishList.getUser().getId() : null);
        wishListDto.setProductId(product.getId());
        log.info("Finished creation of with list with id: {}", savedWishList.getId());
        return wishListDto;
    }
    

    @Override
    public void delete(HttpServletRequest request) {
        var token = request.getHeader("Authorization").substring(7).trim();
        var userId = jwtService.extractUserId(token);
        var user=userRepository.findById(userId)
                .orElseThrow();
        WishList wishList = wishListRepository.findByUser(user)
                .orElseThrow(() -> new DataNotFoundException("Wishlist not found with user: " +  user));

        wishListRepository.delete(wishList);
        log.info("Deleted wishlist with ID {} for customer ID {}", wishList.getId(), userId);
        log.info("Finished deletion of wish list with ID: {} ", wishList.getId());
    }

    @Override
    public void removeProduct(Integer productId, HttpServletRequest request) {
        log.info("Starting removal of product ID: {} from wishlist", productId);

        if (productId == null) {
            throw new IllegalArgumentException("Product ID cannot be null");
        }

        var token = request.getHeader("Authorization").substring(7).trim();
        var userId = jwtService.extractUserId(token);
        User user = userRepository.findById(userId).orElseThrow(
                () -> new DataNotFoundException("Customer not found with id: " + userId));
        WishList wishList = wishListRepository.findByUser(user)
                .orElseThrow(() -> new DataNotFoundException("Wishlist not found with id: " + user.getWishLists().get(0).getId()));
        if (!wishList.getUser().getId().equals(userId)) {
            throw new DataNotFoundException("User not found with id: " + userId);
        }
        boolean removed = wishList.getProducts().removeIf(p -> p.getId().equals(productId));
        if (!removed) {
            throw new DataNotFoundException("Product not found with id: " + productId);
        }

        wishListRepository.save(wishList);
        log.info("Removed product ID: {} ", productId);
        log.info("Finished removal of product from wishlist with ID: {}", productId);
    }
}
