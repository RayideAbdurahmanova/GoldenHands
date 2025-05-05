package com.matrix.Java._Spring.mapper;


import com.matrix.Java._Spring.dto.CreateReviewsRequest;
import com.matrix.Java._Spring.dto.ReviewsDto;
import com.matrix.Java._Spring.model.entity.Reviews;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel="spring")
public interface ReviewsMapper {

    List<ReviewsDto> getReviewsDtoList(List<Reviews> reviews);

    ReviewsDto toReviewsDtoGetById(Reviews reviews);

    Reviews toCreateReviewsRequest(CreateReviewsRequest createReviewsRequest);
}
