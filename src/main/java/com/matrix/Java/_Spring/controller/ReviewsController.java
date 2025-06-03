package com.matrix.Java._Spring.controller;

import com.matrix.Java._Spring.dto.CreateReviewsRequest;
import com.matrix.Java._Spring.dto.ReviewsDto;
import com.matrix.Java._Spring.service.ReviewsService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/reviews")
public class ReviewsController {

    private final ReviewsService reviewsService;

    @GetMapping("/product/{productId}")
    public List<ReviewsDto> getListByProductId(@PathVariable Integer productId) {
        return reviewsService.getListByProductId(productId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReviewsDto create(@RequestBody CreateReviewsRequest createReviewsRequest, HttpServletRequest request) {
        return reviewsService.create(createReviewsRequest,request);
    }

    @PutMapping("/{id}")
    public ReviewsDto update(@PathVariable Integer id,
                             @RequestBody CreateReviewsRequest createReviewsRequest,
                             HttpServletRequest request) {
        return reviewsService.update(id,createReviewsRequest,request);
    }

    @DeleteMapping("/{productId}/{id}")
    public void delete(@PathVariable Integer productId,
                       @PathVariable Integer id,
                       HttpServletRequest request) {
        reviewsService.delete(productId,id,request);
    }
}
