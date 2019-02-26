package com.epam.microservices.productservice.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.epam.microservices.productservice.entity.Product;

@Repository
public interface ProductRepository extends CrudRepository<Product, Long>{

}
