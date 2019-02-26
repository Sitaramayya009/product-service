package com.epam.microservices.productservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.epam.microservices.productservice.entity.Product;
import com.epam.microservices.productservice.repository.ProductRepository;
import java.util.Optional;

@Service
public class ProductService {
	
	@Autowired
	private ProductRepository productRepository;

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
	public Iterable<Product> getAllProducts() {
		return productRepository.findAll();
	}
}
