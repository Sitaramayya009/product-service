package com.epam.microservices.productservice.service;

import com.epam.microservices.productservice.config.ProductReviewClientConfig;
import com.epam.microservices.productservice.dto.Review;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "review-service", configuration = ProductReviewClientConfig.class,
        fallback = ProductReviewFallback.class)
//@FeignClient(name="netflix-zuul-api-gateway-server")
public interface ProductReviewServiceProxy {

    @GetMapping(value = "/{productId}/reviews")
    List<Review> getByProductId(@PathVariable Long productId);

    @PutMapping(value = "/{productId}/reviews/{reviewId}")
    Review updateReview(@RequestBody Review review,
                        @PathVariable Long reviewId, @PathVariable Long prodId);
    @DeleteMapping("/{productId}/reviews/{reviewId}")
    Boolean deleteReview(@PathVariable Long prodId, @PathVariable Long reviewId);

    @PostMapping(value = "/{productId}/reviews")
    List<Review> createReview(@RequestBody List<Review> reviews,
                 @PathVariable Long productId);
}
