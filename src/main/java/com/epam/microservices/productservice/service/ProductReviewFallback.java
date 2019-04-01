package com.epam.microservices.productservice.service;

import com.epam.microservices.productservice.dto.Review;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class ProductReviewFallback implements ProductReviewServiceProxy {

    @Override
    public List<Review> getByProductId(Long productId) {
        return Collections.emptyList();
    }

    @Override
    public Review updateReview(Review review, Long reviewId, Long prodId) {
        return null;
    }

    @Override
    public Boolean deleteReview(Long prodId, Long reviewId) {
        return false;
    }

    @Override
    public List<Review> createReview(List<Review> reviews, Long productId) {
        return Collections.emptyList();
    }
}
