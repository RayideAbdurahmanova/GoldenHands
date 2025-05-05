package com.matrix.Java._Spring.service.impl;


import com.matrix.Java._Spring.dto.CreateReviewsRequest;
import com.matrix.Java._Spring.dto.ReviewsDto;
import com.matrix.Java._Spring.exceptions.DataNotFoundException;
import com.matrix.Java._Spring.mapper.ReviewsMapper;
import com.matrix.Java._Spring.model.entity.Product;
import com.matrix.Java._Spring.model.entity.Reviews;
import com.matrix.Java._Spring.repository.ProductRepository;
import com.matrix.Java._Spring.repository.ReviewsRepository;
import com.matrix.Java._Spring.service.ReviewsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewsService {

    private final ReviewsRepository reviewsRepository;
    private final ReviewsMapper reviewsMapper;
    private final ProductRepository productRepository;

    @Override
    public List<ReviewsDto> getListByProductId(Integer productId) {
        Product product=productRepository.findById(productId)
                .orElseThrow(()->new DataNotFoundException("Product not found"));

        List<Reviews> reviews=reviewsRepository.findByProduct(product);
        return reviewsMapper.getReviewsDtoList(reviews);
    }

//    @Override
//    public ReviewsDto getReviewById(Integer id) {
//        return reviewsMapper.toReviewsDtoGetById(reviewsRepository.findById(id).orElseThrow(
//                ()->new DataNotFoundException("Data Not Found With ID:"+id+" ReviewsID")
//        ));
//    }

    @Override
    public ReviewsDto create(CreateReviewsRequest createReviewsRequest) {
        Reviews reviews =reviewsMapper.toCreateReviewsRequest(createReviewsRequest);
        return reviewsMapper.toReviewsDtoGetById(reviewsRepository.save(reviews));
    }

    @Override
    public ReviewsDto update(Integer id, CreateReviewsRequest createReviewsRequest) {
       Reviews reviews=reviewsRepository.findById(id).orElseThrow(
                ()->new RuntimeException("Review with ID " + id + " not found"));

        Reviews update=reviewsMapper.toCreateReviewsRequest(createReviewsRequest);
        return reviewsMapper.toReviewsDtoGetById(reviewsRepository.save(update));
    }

    @Override
    public void delete(Integer productId,Integer id) {
        Product product=productRepository.findById(productId)
                .orElseThrow(()->new RuntimeException("Review with ID " + id + " not found"));

        Reviews reviews=reviewsRepository.findById(id).orElseThrow(
                ()->new RuntimeException("Review with ID " + id + " not found"));

        reviewsRepository.deleteById(id);

    }
}
