package com.matrix.Java._Spring.dto;


import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CreateReviewsRequest {

    @NotNull(message = "Rating is required")
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating cannot exceed 5")
    private Integer rating;
    @Size(min = 10, max = 200, message = "Comment must be between 10 and 200 characters")
    private String comment;
    @NotNull
    @Positive(message = "Product ID must be positive")
    private Integer productId;
    @NotNull
    @Positive(message = "Order ID must be positive")
    private Integer orderId;

}
