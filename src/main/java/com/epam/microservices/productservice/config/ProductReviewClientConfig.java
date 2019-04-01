package com.epam.microservices.productservice.config;

import com.epam.microservices.productservice.exceptions.ProductReviewUnauthorizedException;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

public class ProductReviewClientConfig {

    @Bean
    public RequestInterceptor interceptor(
            @Value("${review-service.auth.header}") String authHeader,
            @Value("${review-service.auth.value}") String authHeaderValue) {

        return (RequestTemplate template) -> template.header(authHeader, authHeaderValue);
    }

    @Bean
    public ErrorDecoder errorDecoder() {
        final ErrorDecoder defaultErrorDecoder = new ErrorDecoder.Default();

        // TODO: Extract this to an inner class
        return (String methodKey, Response response) -> {
            if (response.status() == 403) {
                return new ProductReviewUnauthorizedException(response.reason());
            }
            return defaultErrorDecoder.decode(methodKey, response);
        };
    }
}
