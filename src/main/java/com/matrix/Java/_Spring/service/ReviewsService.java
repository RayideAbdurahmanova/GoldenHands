package com.matrix.Java._Spring.service;

import com.matrix.Java._Spring.dto.CreateReviewsRequest;
import com.matrix.Java._Spring.dto.ReviewsDto;

import java.util.List;

public interface ReviewsService {

    List<ReviewsDto> getListByProductId(Integer productId);

 //   ReviewsDto getById(Integer id);

    ReviewsDto create( CreateReviewsRequest createReviewsRequest);

    ReviewsDto update(Integer id, CreateReviewsRequest createReviewsRequest);

    void delete(Integer productId,Integer id);
}
