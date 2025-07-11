package com.matrix.Java._Spring.service.impl;

import com.matrix.Java._Spring.dto.CreateProductRequest;
import com.matrix.Java._Spring.dto.ProductDto;
import com.matrix.Java._Spring.exceptions.DataNotFoundException;
import com.matrix.Java._Spring.jwt.JwtService;
import com.matrix.Java._Spring.mapper.ProductMapper;
import com.matrix.Java._Spring.mapper.ReviewsMapper;
import com.matrix.Java._Spring.model.entity.Category;
import com.matrix.Java._Spring.model.entity.Product;
import com.matrix.Java._Spring.model.entity.Seller;
import com.matrix.Java._Spring.model.entity.User;
import com.matrix.Java._Spring.repository.CategoryRepository;
import com.matrix.Java._Spring.repository.CustomerRepository;
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

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private JwtService jwtService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ReviewsMapper reviewsMapper;

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private ProductServiceImpl productService;

    private User user;
    private Seller seller;
    private Category category;
    private Product product;
    private ProductDto productDto;
    private CreateProductRequest createProductRequest;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1);

        seller = new Seller();
        seller.setId(1L);
        user.setSeller(seller);

        category = new Category();
        category.setId(1);

        product = new Product();
        product.setId(1);
        product.setSeller(seller);
        product.setCategory(category);
        product.setProductName("Test Product");
        product.setPrice(100);
        product.setQuantityInStock(5);
        product.setDescription("Test Description");
        product.setImageUrl("Image Url");

        productDto = new ProductDto();
        productDto.setId(1);
        productDto.setProductName("Test Product");
        productDto.setPrice(10.0);
        productDto.setCategoryId(1L);
        productDto.setSellerId(1L);
        productDto.setDescription("Test Description");
        productDto.setQuantityInStock(100);
        productDto.setReviews(new ArrayList<>());

        createProductRequest = new CreateProductRequest();
        createProductRequest.setProductName("Test Product");
        createProductRequest.setPrice(10.0);
        createProductRequest.setCategoryId(1L);
        createProductRequest.setQuantityInStock(100);
        createProductRequest.setDescription("Test Description");
    }

    @AfterEach
    void tearDown() {
        user = null;
        seller = null;
        category = null;
        product = null;
        productDto = null;
        createProductRequest = null;
    }

    @Test
    void getList() {
        List<Product> products = List.of(product);
        List<ProductDto> productDtos = List.of(productDto);
        when(productRepository.findAll()).thenReturn(products);
        when(reviewsMapper.getProductDtoList(products)).thenReturn((productDtos));

        List<ProductDto> result = productService.getList();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getId());
        assertEquals(1L, result.get(0).getCategoryId());
        assertEquals(1, result.get(0).getSellerId());
        verify(productRepository).findAll();
        verify(reviewsMapper).getProductDtoList(products);
    }

    @Test
    void getById() {
        when(productRepository.findById(1)).thenReturn(Optional.ofNullable(product));
        when(productMapper.toProductDtoGetById(product)).thenReturn((productDto));
        ProductDto result = productService.getById(1);
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals(1L, result.getCategoryId());
        assertEquals(1, result.getSellerId());
        verify(productRepository).findById(1);
        verify(productMapper).toProductDtoGetById(product);
    }

    @Test
    void getById_InvalidId() {
        assertThrows(DataNotFoundException.class, () -> productService.getById(-1));

        verify(productRepository, never()).findById(anyInt());
    }

    @Test
    void create() {
        when(request.getHeader("Authorization")).thenReturn("Bearer token");
        when(jwtService.extractUserId("token")).thenReturn(user.getId());
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(categoryRepository.findById(Math.toIntExact(createProductRequest.getCategoryId()))).thenReturn(Optional.of(category));
        when(productMapper.toCreateProductRequest(createProductRequest)).thenReturn(product);
        when(productRepository.save(product)).thenReturn(product);
        when(productMapper.toProductDtoGetById(product)).thenReturn((productDto));

        ProductDto result=productService.create(createProductRequest,request);
        assertNotNull(result);

        verify(jwtService).extractUserId("token");
        verify(userRepository).findById(user.getId());
        verify(categoryRepository).findById(Math.toIntExact(createProductRequest.getCategoryId()));
        verify(productMapper).toCreateProductRequest(createProductRequest);
        verify(productRepository).save(product);
        verify(productMapper).toProductDtoGetById(product);
    }

    @Test
    void update() {
        when(request.getHeader("Authorization")).thenReturn("Bearer token");
        when(jwtService.extractUserId("token")).thenReturn(user.getId());
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));
        when(categoryRepository.findById(Math.toIntExact(createProductRequest.getCategoryId()))).thenReturn(Optional.of(category));
        doNothing().when(productMapper).updateProductFromRequest(createProductRequest, product);
        when(productRepository.save(product)).thenReturn(product);
        when(productMapper.toProductDtoGetById(product)).thenReturn(productDto);

        ProductDto result = productService.update(1, createProductRequest, request);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals(1L, result.getCategoryId());
        assertEquals(1L, result.getSellerId());
        assertEquals("Test Product", result.getProductName());
        assertEquals(10.0, result.getPrice());
        assertEquals("Test Description", result.getDescription());
        assertEquals(100, result.getQuantityInStock());
        verify(request).getHeader("Authorization");
        verify(jwtService).extractUserId("token");
        verify(userRepository).findById(user.getId());
        verify(productRepository).findById(product.getId());
        verify(categoryRepository).findById(Math.toIntExact(createProductRequest.getCategoryId()));
        verify(productMapper).updateProductFromRequest(createProductRequest, product);
        verify(productRepository).save(product);
        verify(productMapper).toProductDtoGetById(product);
    }

    @Test
    void update_NullRequest(){
        when(request.getHeader("Authorization")).thenReturn("Bearer token");

        assertThrows(NoSuchElementException.class, () -> productService.update(1,null,request));

        verify(request).getHeader("Authorization");
    }

    @Test
    void update_invalidId(){
        assertThrows(DataNotFoundException.class,()->productService.update(-1,createProductRequest,request));

        verify(productRepository, never()).findById(anyInt());
    }

    @Test
    void update_ProductNotFound(){
        when(request.getHeader("Authorization")).thenReturn("Bearer token");
        when(jwtService.extractUserId("token")).thenReturn(1);
        when(userRepository.findById(1)).thenReturn(Optional.ofNullable(user));
        when(productRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,()->productService.update(1,createProductRequest,request));
        verify(request).getHeader("Authorization");
        verify(jwtService).extractUserId("token");
        verify(userRepository).findById(1);
        verify(productRepository).findById(1);

    }

    @Test
    void update_CategoryNotFound(){
        when(request.getHeader("Authorization")).thenReturn("Bearer token");
        when(jwtService.extractUserId("token")).thenReturn(1);
        when(userRepository.findById(1)).thenReturn(Optional.ofNullable(user));
        when(productRepository.findById(1)).thenReturn(Optional.ofNullable(product));
        when(categoryRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,()->productService.update(1,createProductRequest,request));

        verify(request).getHeader("Authorization");
        verify(jwtService).extractUserId("token");
        verify(userRepository).findById(1);
        verify(productRepository).findById(1);
        verify(categoryRepository).findById(1);
    }

    @Test
    void delete() {
        when(request.getHeader("Authorization")).thenReturn("Bearer token");
        when(jwtService.extractUserId("token")).thenReturn(1);
        when(userRepository.findById(1)).thenReturn(Optional.ofNullable(user));
        when(productRepository.findById(1)).thenReturn(Optional.ofNullable(product));

        productService.delete(1,request);

        verify(request).getHeader("Authorization");
        verify(jwtService).extractUserId("token");
        verify(userRepository).findById(1);
        verify(productRepository).findById(1);
    }

    @Test
    void delete_invalidId(){
        assertThrows(DataNotFoundException.class,()->productService.delete(-1,request));

        verify(request,never()).getHeader("Authorization");
        verify(productRepository, never()).findById(anyInt());
    }

    @Test
    void delete_ProductNotFound(){
        when(request.getHeader("Authorization")).thenReturn("Bearer token");
        when(jwtService.extractUserId("token")).thenReturn(1);
        when(userRepository.findById(1)).thenReturn(Optional.ofNullable(user));
        when(productRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,()->productService.delete(1,request));
        verify(request).getHeader("Authorization");
        verify(jwtService).extractUserId("token");
        verify(userRepository).findById(1);
        verify(productRepository).findById(1);
    }

    @Test
    void getProductsByCategory() {
        List<Product> products=List.of(product);
        List<ProductDto> productDtos=List.of(productDto);
        when(categoryRepository.findById(1)).thenReturn(Optional.of(category));
        when(productRepository.findByCategory(category)).thenReturn(products);
        when(reviewsMapper.getProductDtoList(products)).thenReturn(productDtos);

        List<ProductDto> result=productService.getProductsByCategory(1);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getId());
        assertEquals(1L, result.get(0).getCategoryId());
        assertEquals(1L, result.get(0).getSellerId());
        assertEquals("Test Product", result.get(0).getProductName());
        assertEquals(10.0, result.get(0).getPrice());
        assertEquals("Test Description", result.get(0).getDescription());
        assertEquals(100, result.get(0).getQuantityInStock());
        verify(categoryRepository).findById(1);
        verify(productRepository).findByCategory(category);
        verify(reviewsMapper).getProductDtoList(products);
    }

    @Test
    void getProductsByCategory_InvalidCategoryId() {
        assertThrows(DataNotFoundException.class, () -> productService.getProductsByCategory(-1),
                "Category ID must be positive");

        verify(categoryRepository, never()).findById(anyInt());
    }

    @Test
    void getProductsByCategory_CategoryNotFound() {
        when(categoryRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(DataNotFoundException.class, () -> productService.getProductsByCategory(1),
                "Category not found with ID: 1");

        verify(categoryRepository).findById(1);
        verify(productRepository, never()).findByCategory(any());
    }


    @Test
    void getProductsBySeller() {
        List<Product> products = List.of(product);
        List<ProductDto> productDtos = List.of(productDto);
        when(productRepository.findBySellerId(1)).thenReturn(products);
        when(reviewsMapper.getProductDtoList(products)).thenReturn(productDtos);

        List<ProductDto> result = productService.getProductsBySeller(1);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getId());
        assertEquals(1L, result.get(0).getCategoryId());
        assertEquals(1L, result.get(0).getSellerId());
        assertEquals("Test Product", result.get(0).getProductName());
        assertEquals(10.0, result.get(0).getPrice());
        assertEquals("Test Description", result.get(0).getDescription());
        assertEquals(100, result.get(0).getQuantityInStock());
        verify(productRepository).findBySellerId(1);
        verify(reviewsMapper).getProductDtoList(products);
    }

    @Test
    void getProductsBySeller_InvalidSellerId_ThrowsDataNotFoundException() {
        assertThrows(DataNotFoundException.class, () -> productService.getProductsBySeller(-1),
                "Seller ID must be positive");

        verify(productRepository, never()).findBySellerId(anyInt());
    }

}