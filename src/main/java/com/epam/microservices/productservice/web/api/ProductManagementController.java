package com.epam.microservices.productservice.web.api;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import com.epam.microservices.productservice.exceptions.ProductNotFoundException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.epam.microservices.productservice.entity.Product;
import com.epam.microservices.productservice.exceptions.ProductServiceException;
import com.epam.microservices.productservice.service.ProductService;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("products")
@Api(value = "Product Resource", description = "Shows Products")
public class ProductManagementController {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private ProductService productService;

	@ApiOperation("Retrive Products List")
	@GetMapping
	public ResponseEntity<Iterable<Product>> findAllProdcuts() {
		return new ResponseEntity<>(productService.getAllProducts(), HttpStatus.OK);
	}

	@ApiOperation("Retrive Product")
	@GetMapping("/{id}")
	public ResponseEntity<Product> findProdcut(@PathVariable("id") long id) {

		Optional<Product> product =  productService.getProductById(id);
		if (!product.isPresent())
			throw new ProductNotFoundException("Product not found for id " + id);
		return new ResponseEntity<Product>(product.get(), HttpStatus.OK);
	}

	@ApiOperation("Create Product")
	@PostMapping()
	public ResponseEntity<?> createProduct(@RequestBody Product product) {
		/*if (productService.isProductExist(product)) {
			logger.error("Unable to create. A Product with name {} already exist", product.getName());
			return new ResponseEntity(new ProductServiceException("Unable to create. A Product with name " + 
					product.getName() + " already exist."),HttpStatus.CONFLICT);
		}*/
		Product savedProduct = productService.saveorUpdateProduct(product);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(savedProduct.getId()).toUri();
		return ResponseEntity.created(location).build();
	}

	@ApiOperation("Update Product")
	@PutMapping("/{id}")
	public ResponseEntity<?> updateProduct(@PathVariable Long id,@RequestBody Product product) {
		logger.info("Updating Product with id {}", id);

		Optional<Product> currentProduct = productService.getProductById(id);

		if (currentProduct.isPresent()) {
			currentProduct.get().setName(product.getName());
			currentProduct.get().setCreatedDate(product.getCreatedDate());
			Product updatedProduct = productService.saveorUpdateProduct(currentProduct.get());
			return new ResponseEntity<Product>(updatedProduct,HttpStatus.OK); 
		}else {
			logger.error("Unable to update. Product with id {} not found.", id);
			return new ResponseEntity(new ProductServiceException("Unable to upate. Product with id " + id + " not found."),
					HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping("/{id}")
	@ApiOperation("Delete Product")
	public ResponseEntity<?> deleteProduct(@PathVariable("id") long id) {
		Optional<Product> product = productService.getProductById(id);
		if (!product.isPresent())
			throw new ProductNotFoundException("Product not found for id " + id);
		productService.deleteProduct(id);
		return new ResponseEntity<Product>(HttpStatus.NO_CONTENT);
	}
}
