package com.epam.microservices.productservice.service;

import com.epam.microservices.productservice.dto.Review;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

import com.epam.microservices.productservice.entity.Product;
import com.epam.microservices.productservice.repository.ProductRepository;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class ProductService {
	
	@Autowired
	private ProductRepository productRepository;

//	@Autowired
	RestTemplate restTemplate;

	@Value("${product.review.url}")
	private String review_service_uri;

	public boolean isProductExist(Product product) {
		return false;
	}

	public Product saveorUpdateProduct(Product product) {
		Product savedProduct = productRepository.save(product);
		return savedProduct;
	}

	public Product updateProduct(Product product) {
		Product updatedProduct = productRepository.save(product);
		return updatedProduct;
	}
	
	public void deleteProduct(Long id) {
		productRepository.deleteById(id);
	}
	
	public Optional<Product> getProductById(Long id) {
		return productRepository.findById(id);
	}

	public Product getProductAndReviewsById(Long id) {
		Optional<Product> product = productRepository.findById(id);
		if(product.isPresent()){
			Product product1 = product.get();
			product1.setProdReviews(retrieveProductReviewsData(product1.getId()));
			return product1;
		}
		return product.get();
	}
	public Iterable<Product> getAllProducts() {
		Iterable<Product> products = productRepository.findAll();
		for(Product product : products){
			product.setProdReviews(retrieveProductReviewsData(product.getId()));
		}
		return products;
	}


	public List<Review> retrieveProductReviewsData(Long productId){
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.set("SharedSecret", "BasicAuth");
		HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
		restTemplate = new RestTemplate();
		ResponseEntity<List<Review>> response = restTemplate.exchange(review_service_uri+"/{productId}/reviews", HttpMethod.GET, entity, new ParameterizedTypeReference<List<Review>>() {
		}, productId);
		return response.getBody();
	}

	public Product saveProductReviews(List<Review> reviews, Long productId) {
		Optional<Product> product = productRepository.findById(productId);
		if(product.isPresent()) {
			restTemplate = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
			headers.set("SharedSecret", "BasicAuth");
			HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);

			ResponseEntity<List<Review>> response = restTemplate.exchange(review_service_uri+"/{productId}/reviews", HttpMethod.POST, entity, new ParameterizedTypeReference<List<Review>>() {
			}, productId);
			List<Review> prodReviews = response.getBody();
			Product productWithReview = product.get();
			productWithReview.setProdReviews(prodReviews);
			return productWithReview;
		}
		return product.get();
	}

	public boolean deleteProductReview(Long productId, Long reviewId) {
		Optional<Product> product = productRepository.findById(productId);
		if(product.isPresent()) {
			restTemplate = new RestTemplate();
			Map<String, Long> params = new HashMap<String, Long>();
			params.put("productId", productId);
			params.put("reviewId",reviewId);

			restTemplate.delete(review_service_uri+"/{productId}/reviews/{reviewId}",params);
			return true;
		}
		return false;
	}

	public Product updateProductReview(Review review, Long reviewId, Long productId) {
		Optional<Product> product = productRepository.findById(productId);
		if(product.isPresent()) {
			restTemplate = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
			headers.set("SharedSecret", "BasicAuth");
			HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);

			ResponseEntity<Review> response = restTemplate.exchange(review_service_uri+"/{productId}/reviews/{reviewId}", HttpMethod.PUT, entity, new ParameterizedTypeReference<Review>() {
			}, productId);
			Review prodReview = response.getBody();
			List<Review> prodReviews = new ArrayList<>();
			prodReviews.add(prodReview);
			Product productWithReview = product.get();
			productWithReview.setProdReviews(prodReviews);
			return productWithReview;
		}
		return product.get();
	}
}
