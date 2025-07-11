package com.matrix.Java._Spring.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UpdateReviewsRequest {

    @NotNull(message = "Review ID is required")
    @Positive(message = "ID must be positive")
    private Integer reviewId;

    @NotNull(message = "Rating is required")
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating cannot exceed 5")
    @Positive(message = "Rating must be positive")
    private Integer rating;

    @Size(min = 10, max = 200, message = "Comment must be between 10 and 200 characters")
    private String comment;

    @NotNull(message = "Product ID is required")
    @Positive(message = "ID must be positive")
    private Integer productId;
}
