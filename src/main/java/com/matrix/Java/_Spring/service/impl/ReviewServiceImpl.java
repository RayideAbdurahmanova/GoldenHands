package com.matrix.Java._Spring.service.impl;


import com.matrix.Java._Spring.dto.CreateReviewsRequest;
import com.matrix.Java._Spring.dto.ReviewsDto;
import com.matrix.Java._Spring.exceptions.DataNotFoundException;
import com.matrix.Java._Spring.jwt.JwtService;
import com.matrix.Java._Spring.mapper.ReviewsMapper;
import com.matrix.Java._Spring.model.entity.Product;
import com.matrix.Java._Spring.model.entity.Reviews;
import com.matrix.Java._Spring.model.entity.User;
import com.matrix.Java._Spring.repository.CustomerRepository;
import com.matrix.Java._Spring.repository.ProductRepository;
import com.matrix.Java._Spring.repository.ReviewsRepository;
import com.matrix.Java._Spring.repository.UserRepository;
import com.matrix.Java._Spring.service.ReviewsService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewsService {

    private final ReviewsRepository reviewsRepository;
    private final ReviewsMapper reviewsMapper;
    private final ProductRepository productRepository;
    private final JwtService jwtService;
    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;

    @Override
    public List<ReviewsDto> getListByProductId(Integer productId) {
        log.info("Starting retrieval list of reviews with ID: {}", productId);
        Product product=productRepository.findById(productId)
                .orElseThrow(()->new DataNotFoundException("Product not found"));

        List<Reviews> reviews=reviewsRepository.findByProduct(product);
        List<ReviewsDto> reviewsDtos=reviewsMapper.getReviewsDtoList(reviews);
        log.info("Finished retrieval {} reviews with ID: {}",reviewsDtos.size(), productId);
        return reviewsDtos;
    }

    @Override
    public ReviewsDto create(CreateReviewsRequest createReviewsRequest, HttpServletRequest request) {
        log.info("Starting creation of review : {}", createReviewsRequest);
        var token=request.getHeader("Authorization").substring(7).trim();
        var userId=jwtService.extractUserId(token);
        User user=userRepository.findById(userId)
                .orElseThrow(()->new DataNotFoundException("Customer not found"));
        Product product=productRepository.findById(createReviewsRequest.getProductId())
                .orElseThrow(()->new DataNotFoundException("Product not found"));
         Reviews reviews=reviewsMapper.toCreateReviewsRequest(createReviewsRequest);
         reviews.setUser(user);
         reviews.setProduct(product);
         Reviews saved=reviewsRepository.save(reviews);
        log.info("Created review with ID {} for product ID {} by customer ID {}", saved.getReviewId(), product.getId(), userId);
        ReviewsDto reviewsDto=reviewsMapper.toReviewsDtoGetById(saved);
        log.info("Finished creation of review with ID {}", saved.getReviewId());
        return reviewsDto;
    }

    @Override
    public ReviewsDto update(Integer id, CreateReviewsRequest createReviewsRequest,HttpServletRequest request) {
        log.info("Starting update of review: {}", createReviewsRequest);
        var token=request.getHeader("Authorization").substring(7).trim();
        var userId=jwtService.extractUserId(token);
        Reviews reviews=reviewsRepository.findById(id)
                .orElseThrow(()->new DataNotFoundException("Customer not found"));
        if(!reviews.getUser().getId().equals(userId)){
            throw new AccessDeniedException("You do not have permission to update this review");
        }
        Product product=productRepository.findById(createReviewsRequest.getProductId())
                .orElseThrow(()->new DataNotFoundException("Product not found"));
        reviewsMapper.updateCreateReviewsRequest(createReviewsRequest,reviewsMapper);
        productRepository.save(product);
        Reviews updated= reviewsRepository.save(reviews);
        log.info("Updated review with ID {} for product ID {} by customer ID {}", id, product.getId(), userId);
        ReviewsDto reviewsDto=reviewsMapper.toReviewsDtoGetById(updated);
        log.info("Finished update of product with ID {} successfully", updated.getReviewId());
        return reviewsDto;
    }

    @Override
    public void delete(Integer productId,Integer id,HttpServletRequest request) {
        log.info("Starting deletion of review: {}", id);
        var token=request.getHeader("Authorization").substring(7).trim();
        var userId=jwtService.extractUserId(token);
        Product product=productRepository.findById(productId)
                .orElseThrow(()->new RuntimeException("Review with ID " + id + " not found"));
        Reviews reviews=reviewsRepository.findById(id).orElseThrow(
                ()->new RuntimeException("Review with ID " + id + " not found"));
        User user=userRepository.findById(userId)
                        .orElseThrow(()->new RuntimeException("Customer not found"));
        if(!reviews.getUser().getId().equals(userId)){
            throw new AccessDeniedException("You do not have permission to delete this review");
        }
        reviewsRepository.deleteById(id);
        log.info("Deleted review with ID {} for product ID {} by customer ID {}", id, productId, userId);
        log.info("Finished deletion of product with ID {} successfully", productId);
    }
}
