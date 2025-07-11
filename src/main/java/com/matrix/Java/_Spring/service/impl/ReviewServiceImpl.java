package com.matrix.Java._Spring.service.impl;


import com.matrix.Java._Spring.dto.CreateReviewsRequest;
import com.matrix.Java._Spring.dto.ReviewsDto;
import com.matrix.Java._Spring.exceptions.DataNotFoundException;
import com.matrix.Java._Spring.jwt.JwtService;
import com.matrix.Java._Spring.mapper.ReviewsMapper;
import com.matrix.Java._Spring.model.entity.Product;
import com.matrix.Java._Spring.model.entity.Reviews;
import com.matrix.Java._Spring.model.entity.User;
import com.matrix.Java._Spring.repository.*;
import com.matrix.Java._Spring.service.ReviewsService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewsService {

    private final ReviewsRepository reviewsRepository;
    private final ReviewsMapper reviewsMapper;
    private final ProductRepository productRepository;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final OrderProductRepository orderProductRepository;

    @Override
    public List<ReviewsDto> getListByProductId(Integer productId) {
        log.info("Starting retrieval list of reviews with ID: {}", productId);
        if (productId == null || productId <= 0) {
            log.error("Product ID cannot be null or negative");
            throw new IllegalArgumentException("Product ID cannot be null or negative");
        }

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new DataNotFoundException("Product not found"));

        List<Reviews> reviews = reviewsRepository.findByProduct(product);
        if (reviews.isEmpty()) {
            log.error("No reviews found for product ID: {}", productId);
        }
        List<ReviewsDto> reviewsDtos = reviewsMapper.getReviewsDtoList(reviews);
        log.info("Finished retrieval {} reviews with ID: {}", reviewsDtos.size(), productId);
        return reviewsDtos;
    }

    @Override
    public List<ReviewsDto> getListByUserId() {

        var email = SecurityContextHolder.getContext().getAuthentication().getName();
        if (email == null) {
            log.error("No authenticated user found");
            throw new IllegalStateException("User not authenticated");
        }
        var user = userRepository.findByUsername(email)
                .orElseThrow(
                        () -> new DataNotFoundException("User not found")
                );

        List<Reviews> reviews = reviewsRepository.findByUser(user);
        if (reviews.isEmpty()) {
            log.error("No reviews found for user ID: {}", user.getId());
        }

        List<ReviewsDto> reviewsDtos=reviews.stream()
                .map(rw->{
                    ReviewsDto reviewsDto=new ReviewsDto();
                    reviewsDto.setRating(rw.getRating());
                    reviewsDto.setComment(rw.getComment());
                    rw.setReviewId(rw.getReviewId());
                    if(rw.getProduct() != null) {
                        reviewsDto.setProductId(rw.getProduct().getId());
                    }
                    return reviewsDto;
                })
                .collect(Collectors.toList());

        log.info("Finished retrieval of {} reviews for user ID: {}", reviewsDtos.size(), user.getId());
        return reviewsDtos;
    }


    @Override
    public ReviewsDto create(CreateReviewsRequest createReviewsRequest, HttpServletRequest request) {
        log.info("Starting creation of review : {}", createReviewsRequest);
        var token = request.getHeader("Authorization").substring(7).trim();
        var userId = jwtService.extractUserId(token);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException("Customer not found"));
        var order = orderRepository
                .findByOrderIdAndCustomerCustomerId(createReviewsRequest.getOrderId()
                        , user.getCustomer().getCustomerId())
                .orElseThrow(() -> new DataNotFoundException("Order not found"));
        var orderProducts = orderProductRepository
                .findByOrderIdAndProductId(order.getOrderId(), createReviewsRequest.getProductId())
                .orElseThrow(() -> new DataNotFoundException("Product not found"));

        if (reviewsRepository.existsByUserAndProduct(user, orderProducts.getProduct())) {
            throw new IllegalStateException("User has already reviewed this product");
        }
        Reviews reviews = reviewsMapper.toCreateReviewsRequest(createReviewsRequest);
        reviews.setUser(user);
        reviews.setProduct(orderProducts.getProduct());

        if (reviews.getCreatedAt() == null) {
            reviews.setCreatedAt(LocalDateTime.now());
        }

        Reviews saved = reviewsRepository.save(reviews);
        log.info("Created review with ID {} for product ID {} by customer ID {}", saved.getReviewId(), orderProducts.getProduct().getId(), userId);
        ReviewsDto reviewsDto = reviewsMapper.toReviewsDtoGetById(saved);
        log.info("Finished creation of review with ID {}", saved.getReviewId());
        return reviewsDto;
    }

    @Override
    public ReviewsDto update(Integer id, CreateReviewsRequest createReviewsRequest, HttpServletRequest request) {
        log.info("Starting update of review ID: {}", id);
        if (id == null || id<=0) {
            log.error("Review ID cannot be null or negative");
            throw new IllegalArgumentException("Review ID is required positive number");
        }
        var token = request.getHeader("Authorization").substring(7).trim();
        var userId = jwtService.extractUserId(token);
        Reviews reviews = reviewsRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Review not found"));
        if (!reviews.getUser().getId().equals(userId)) {
            log.warn("User ID {} attempted to update review ID {} owned by another user", userId, id);
            throw new AccessDeniedException("You do not have permission to update this review");
        }
        if (!reviews.getProduct().getId().equals(createReviewsRequest.getProductId())) {
            log.error("Product ID {} does not match review's product ID {}",
                    createReviewsRequest.getProductId(), reviews.getProduct().getId());
            throw new IllegalArgumentException("Product ID does not match review's product");
        }

        if (createReviewsRequest.getComment() != null && !createReviewsRequest.getComment().isEmpty()) {
            LocalDateTime now = LocalDateTime.now();
            long hoursSinceCreation = ChronoUnit.HOURS.between(reviews.getCreatedAt(), now);
            if (hoursSinceCreation > 72) {
                log.warn("Attempted to update comment for review ID {} after 72 hours", id);
                throw new IllegalStateException("Comment can only be updated within 72 hours of creation");
            }
            reviews.setComment(createReviewsRequest.getComment());
            reviews.setRating(createReviewsRequest.getRating());
        }


        Reviews updated = reviewsRepository.save(reviews);
        log.info("Updated review with ID {} for product ID {} by customer ID {}", id, createReviewsRequest.getProductId(), userId);
        ReviewsDto reviewsDto = reviewsMapper.toReviewsDtoGetById(updated);
        reviewsDto.setProductId(updated.getProduct().getId());
        log.info("Finished update of product with ID {} successfully", updated.getReviewId());
        return reviewsDto;
    }

    @Override
    public void delete(Integer productId, HttpServletRequest request) {
        if(productId<=0){
            throw new DataNotFoundException("ID must be positive");
        }
        var token = request.getHeader("Authorization").substring(7).trim();
        var userId = jwtService.extractUserId(token);
        var review = reviewsRepository.findByUserIdAndProductId(userId, productId)
                .orElseThrow(() -> new DataNotFoundException("Review not found"));

        reviewsRepository.delete(review);
        log.info("Deleted review with ID {} for product ID {} by customer ID {}", review.getReviewId(), productId, userId);
        log.info("Finished deletion of product with ID {} successfully", productId);
    }
}
