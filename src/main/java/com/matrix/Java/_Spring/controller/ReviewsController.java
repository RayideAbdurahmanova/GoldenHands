package com.matrix.Java._Spring.controller;

import com.matrix.Java._Spring.dto.CreateReviewsRequest;
import com.matrix.Java._Spring.dto.ReviewsDto;
import com.matrix.Java._Spring.service.ReviewsService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/reviews")
public class ReviewsController {

    private final ReviewsService reviewsService;

    @GetMapping("/product/{productId}")
    @PreAuthorize("hasAnyAuthority('USER','ADMIN','SELLER')")
    @ResponseStatus(HttpStatus.OK)
    public List<ReviewsDto> getListByProductId(@PathVariable Integer productId) {
        return reviewsService.getListByProductId(productId);
    }

    @GetMapping("/user")
    @PreAuthorize("hasAnyAuthority('USER')")
    @ResponseStatus(HttpStatus.OK)
    public List<ReviewsDto> getListByUserId() {
        return reviewsService.getListByUserId();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('USER')")
    public ReviewsDto create(@Valid @RequestBody CreateReviewsRequest createReviewsRequest, HttpServletRequest request) {
        return reviewsService.create(createReviewsRequest, request);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('USER')")
    public ReviewsDto update(@PathVariable Integer id,
                             @RequestBody CreateReviewsRequest createReviewsRequest,
                             HttpServletRequest request) {
        return reviewsService.update(id, createReviewsRequest, request);
    }

    @DeleteMapping("/{productId}")
    @PreAuthorize("hasAnyAuthority('USER','SELLER')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer productId,
                       HttpServletRequest request) {
        reviewsService.delete(productId, request);
    }
}
