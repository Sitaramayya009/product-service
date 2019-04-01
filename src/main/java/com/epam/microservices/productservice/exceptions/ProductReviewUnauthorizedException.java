package com.epam.microservices.productservice.exceptions;

public class ProductReviewUnauthorizedException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    public ProductReviewUnauthorizedException(String message) {
        super(message);
    }
}
