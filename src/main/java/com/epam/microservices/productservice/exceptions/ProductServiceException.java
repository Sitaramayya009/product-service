package com.epam.microservices.productservice.exceptions;

public class ProductServiceException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public ProductServiceException(String message) {
		super(message);
	}
}
