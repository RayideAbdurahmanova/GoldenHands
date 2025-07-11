package com.matrix.Java._Spring.service.impl;


import com.matrix.Java._Spring.dto.BasketDto;
import com.matrix.Java._Spring.dto.BasketItemDto;
import com.matrix.Java._Spring.dto.BasketRequest;
import com.matrix.Java._Spring.exceptions.DataNotFoundException;
import com.matrix.Java._Spring.jwt.JwtService;
import com.matrix.Java._Spring.mapper.BasketMapper;
import com.matrix.Java._Spring.model.entity.Basket;
import com.matrix.Java._Spring.model.entity.BasketItem;
import com.matrix.Java._Spring.model.entity.Product;
import com.matrix.Java._Spring.repository.BasketRepository;
import com.matrix.Java._Spring.repository.ProductRepository;
import com.matrix.Java._Spring.repository.UserRepository;
import com.matrix.Java._Spring.service.BasketService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BasketServiceImpl implements BasketService {

    private final JwtService jwtService;
    private final BasketRepository basketRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final BasketMapper basketMapper;

    @Override
    @Transactional
    public void addBasket(HttpServletRequest req, BasketRequest basketRequest) {
        var token = req.getHeader("Authorization").trim().substring(7);
        var userId = jwtService.extractUserId(token);
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User Not Found"));
        var customer = user.getCustomer();
        if (customer == null) {
            throw new RuntimeException("Customer Not Found");
        }
        var basket = basketRepository.findByCustomer_CustomerId(customer.getCustomerId());
        var product = productRepository.findById(basketRequest.getProductId())
                .orElseThrow(() -> new RuntimeException("Product Not Found"));

        if (basket == null) {
            basket = new Basket();
            basket.setCustomer(customer);
            BasketItem basketItem = new BasketItem();
            basketItem.setQuantity(basketRequest.getQuantity());
            basketItem.setBasket(basket);
            basketItem.setProduct(product);
            basket.getItems().add(basketItem);
            Integer quantity = basketRequest.getQuantity();
            Integer productId = basketRequest.getProductId();
            basket.setTotalPrice(calculateTotalAmount(productId, quantity));
            basketRepository.save(basket);
        } else {
            BasketItem existingItem = basket.getItems().stream()
                    .filter(item -> item.getProduct().getId().equals(product.getId()))
                    .findFirst()
                    .orElse(null);

            if (existingItem != null) {
                existingItem.setQuantity(existingItem.getQuantity() + basketRequest.getQuantity());
            } else {
                BasketItem newItem = new BasketItem();
                newItem.setQuantity(basketRequest.getQuantity());
                newItem.setBasket(basket);
                newItem.setProduct(product);
                basket.getItems().add(newItem);
            }

            basket.setTotalPrice(calculateTotalAmount(basketRequest.getProductId(), basketRequest.getQuantity()).add(basket.getTotalPrice()));
            basket.setUpdatedAt(LocalDateTime.now());
            basketRepository.save(basket);
        }
    }

    @Override
    public void delete(HttpServletRequest req, Integer productId) {
        if(productId == null||productId <= 0) {
            throw new DataNotFoundException("Product Id Not Found");
        }
        var token = req.getHeader("Authorization").trim().substring(7);
        var userId = jwtService.extractUserId(token);
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User Not Found"));
        var customer = user.getCustomer();
        var basket = basketRepository.findByCustomer_CustomerId(customer.getCustomerId());
        var itemBasket = basket.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new DataNotFoundException("Product not found"));
        if (itemBasket.getQuantity() > 0) {
            int quantity = 1;
            var countQuantity = itemBasket.getQuantity() - quantity;
            itemBasket.setQuantity(countQuantity);
            var price = basket.getTotalPrice();

            var updatePrice = price.subtract(calculateTotalAmount(productId, quantity));
            basket.setTotalPrice(updatePrice);
            basketRepository.save(basket);
        } else
            throw new RuntimeException("Basket is empty");
    }

    @Override
    @Transactional
    public BasketDto getBasket(HttpServletRequest req) {
        var token = req.getHeader("Authorization").trim().substring(7);
        var userId = jwtService.extractUserId(token);
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User Not Found"));
        Basket basket = basketRepository.findByCustomer_CustomerId(user.getCustomer().getCustomerId());
        if (basket == null) {
            throw new RuntimeException("Basket not found for this user");
        }
        Hibernate.initialize(basket.getItems());
        basket.getItems().forEach(item -> Hibernate.initialize(item.getProduct()));
        BasketDto basketDto = basketMapper.toDto(basket);
        List<BasketItemDto> itemDtos = basket.getItems().stream()
                .map(item -> {
                    BasketItemDto itemDto = new BasketItemDto();
                    itemDto.setId(item.getId());
                    itemDto.setQuantity(item.getQuantity());
                    if (item.getProduct() != null) {
                        itemDto.setProductId(item.getProduct().getId());
                    } else {
                        System.err.println("Warning: Null product for BasketItem ID: " + item.getId());
                        itemDto.setProductId(null);
                        log.info("Product is null for BasketItem ID: " + item.getId());
                    }
                    return itemDto;

                }).collect(Collectors.toList());
        basketDto.setItems(itemDtos);
        return basketDto;
    }


    private BigDecimal calculateTotalAmount(Integer productId, Integer quantity) {
        BigDecimal totalAmount = BigDecimal.ZERO;
        Product product = productRepository.findById(Math.toIntExact(productId))
                .orElseThrow(() -> new DataNotFoundException("Product not found with ID: " + productId));
        BigDecimal price = BigDecimal.valueOf(product.getPrice());
        if (quantity > product.getQuantityInStock()) {
            throw new DataNotFoundException("Quantity Exceeds Stock");
        }
        totalAmount = totalAmount.add(price.multiply(BigDecimal.valueOf(quantity)));
        log.info("Finished calculation total amount {} for order with product ID {}", totalAmount, productId);
        return totalAmount;
    }
}
