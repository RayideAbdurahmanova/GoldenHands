package com.matrix.Java._Spring.controller;

import com.matrix.Java._Spring.dto.CreateReviewsRequest;
import com.matrix.Java._Spring.dto.ReviewsDto;
import com.matrix.Java._Spring.exceptions.DataNotFoundException;
import com.matrix.Java._Spring.model.entity.Product;
import com.matrix.Java._Spring.model.entity.Reviews;
import com.matrix.Java._Spring.service.ReviewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/reviews")
public class ReviewsController {

    private final ReviewsService reviewsService;

    @GetMapping("/ByProductId/{productId}")
    public List<ReviewsDto> getListByProductId(@PathVariable Integer productId) {
        return reviewsService.getListByProductId(productId);
    }

//    @Override
//    public ReviewsDto getReviewById(Integer id) {
//        return reviewsMapper.toReviewsDtoGetById(reviewsRepository.findById(id).orElseThrow(
//                ()->new DataNotFoundException("Data Not Found With ID:"+id+" ReviewsID")
//        ));
//    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReviewsDto create(@RequestBody CreateReviewsRequest createReviewsRequest) {
        return reviewsService.create(createReviewsRequest);
    }


    @PutMapping("/update/{id}")
    public ReviewsDto update(@PathVariable Integer id,
                             @RequestBody CreateReviewsRequest createReviewsRequest) {
        return reviewsService.update(id,createReviewsRequest);
    }


    @DeleteMapping("/delete/{prodductId}/{id}")
    public void delete(@PathVariable Integer productId,@PathVariable Integer id) {
        reviewsService.delete(productId,id);
    }
}
