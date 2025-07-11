package com.matrix.Java._Spring.service.impl;

import com.matrix.Java._Spring.dto.*;
import com.matrix.Java._Spring.jwt.JwtService;
import com.matrix.Java._Spring.mapper.BasketMapper;
import com.matrix.Java._Spring.mapper.CategoryMapper;
import com.matrix.Java._Spring.model.entity.*;
import com.matrix.Java._Spring.repository.BasketRepository;
import com.matrix.Java._Spring.repository.CategoryRepository;
import com.matrix.Java._Spring.repository.ProductRepository;
import com.matrix.Java._Spring.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BasketServiceImplTest {

    private Basket basket;
    private User user;
    private Customer customer;
    private Product product;
    private BasketDto basketDto;
    private BasketItem  basketItem;
    private BasketItemDto basketItemDto;
    private BasketRequest basketRequest;
    @InjectMocks
    private BasketServiceImpl basketService;
    @Mock
    private BasketRepository basketRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private BasketMapper basketMapper;
    @Mock
    private JwtService jwtService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private HttpServletRequest request;


    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1);

        customer = new Customer();
        customer.setCustomerId(1);

        user.setCustomer(customer);

        product = new Product();
        product.setId(1);
        product.setPrice(100.0);
        product.setQuantityInStock(10);

        basket = new Basket();
        basket.setCustomer(customer);
        basket.setTotalPrice(BigDecimal.ZERO);
        basket.setItems(new ArrayList<>());

        basketItem = new BasketItem();
        basketItem.setId(1L);
        basketItem.setQuantity(2);
        basketItem.setProduct(product);
        basketItem.setBasket(basket);

        basketDto = new BasketDto();
        basketDto.setTotalPrice(BigDecimal.ZERO);

        basketItemDto = new BasketItemDto();
        basketItemDto.setId(1L);
        basketItemDto.setQuantity(2);
        basketItemDto.setProductId(1);

        basketRequest = new BasketRequest();
        basketRequest.setProductId(1);
        basketRequest.setQuantity(2);

    }

    @AfterEach
    void tearDown() {
        user=null;
        customer=null;
        product=null;
        basket=null;
        basketItem=null;
        basketItemDto=null;
        basketRequest=null;
    }

    @Test
    void addBasket() {
        when(request.getHeader("Authorization")).thenReturn("Bearer token");
        when(jwtService.extractUserId("token")).thenReturn(1);
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(basketRepository.findByCustomer_CustomerId(1)).thenReturn(basket);
        when(productRepository.findById(1)).thenReturn(Optional.of(product));
        when(basketRepository.save(any(Basket.class))).thenReturn(basket);

        basketService.addBasket(request, basketRequest);

        assertEquals(1, basket.getItems().size());
        assertEquals(2, basket.getItems().get(0).getQuantity());
        assertEquals(BigDecimal.valueOf(200.0), basket.getTotalPrice());
        assertNotNull(basket.getUpdatedAt());
        verify(jwtService).extractUserId("token");
        verify(userRepository).findById(1);
        verify(basketRepository).findByCustomer_CustomerId(1);
        verify(productRepository).findById(1);
        verify(basketRepository).save(any(Basket.class));
    }

    @Test
    void delete() {
    }

    @Test
    void getBasket() {
    }
}