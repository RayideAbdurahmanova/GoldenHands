package com.matrix.Java._Spring.service;

import com.matrix.Java._Spring.dto.CreateReviewsRequest;
import com.matrix.Java._Spring.dto.ReviewsDto;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface ReviewsService {

    List<ReviewsDto> getListByProductId(Integer productId);

    List<ReviewsDto> getListByUserId();

    ReviewsDto create(CreateReviewsRequest createReviewsRequest, HttpServletRequest request);

    ReviewsDto update(Integer id, CreateReviewsRequest createReviewsRequest,HttpServletRequest request);

    void delete(Integer productId, HttpServletRequest request);
}
