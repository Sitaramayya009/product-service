package com.epam.microservices.productservice.service;

import com.epam.microservices.productservice.entity.Product;
import com.epam.microservices.productservice.repository.ProductRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.stereotype.Service;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@WebMvcTest(ProductService.class)
public class ProductServiceTest {

	@Autowired
	MockMvc mockMvc;

	@InjectMocks
	ProductService productService;

	@MockBean
	ProductRepository productRepository;

	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
		mockMvc = MockMvcBuilders
				.standaloneSetup(productService)
				.build();
	}

	@Test
	public void shouldReturnProductList() throws Exception {
		Iterable<Product> taskList = Arrays.asList(retriveProductMockData());
		when(productRepository.findAll()).thenReturn(taskList);
		Iterable<Product> result = productService.getAllProducts();
		Assert.assertEquals(1, result.spliterator().getExactSizeIfKnown());
	}

	private Product retriveProductMockData() {
		return new Product(20001L,"iPhone",null,"Mobile",65000.00,null);
	}

}
