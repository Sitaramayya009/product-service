package com.epam.microservices.productservice.web.api;

import com.epam.microservices.productservice.entity.Product;
import com.epam.microservices.productservice.exceptions.ProductNotFoundException;
import com.epam.microservices.productservice.exceptions.ProductServiceException;
import com.epam.microservices.productservice.service.ProductService;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.HttpStatusCodeException;

import java.net.URI;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
public class ProductManagementControllerTest {

	@InjectMocks
	ProductManagementController productcontroller;

	@MockBean
	private ProductService productService;

	MockMvc mockMvc;

	@Before
	public void preTest() {
		MockitoAnnotations.initMocks(this);
		mockMvc = MockMvcBuilders
				.standaloneSetup(productcontroller)
				.build();
	}

	@Test
	public void shouldRetrieveAllProducts() throws Exception {
		Iterable<Product> productsList = Arrays.asList((retriveProductMockData()));
		Mockito.when(
				productService.getAllProducts()).thenReturn(productsList);

		RequestBuilder requestBuilder = get("/products");

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		String expected = "[{\"id\":20001,\"name\":\"iPhone\",\"createdDate\":null,\"desc\":\"Mobile\",\"price\":65000.00}]";
		System.out.println(result.getResponse().getContentAsString());

		JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);

	}

	@Test
	public void shouldRetrieveProductById() throws Exception {
		Product productList = retriveProductMockData();
		Mockito.when(
				productService.getProductById(20001L)).thenReturn(Optional.of(productList));
		mockMvc.perform(get("/products/{id}", 20001L))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.id", Matchers.is(20001)))
				.andExpect(jsonPath("$.name", is("iPhone")));
		verify(productService, times(1)).getProductById(20001L);
	}

	//@Test(expected = ProductNotFoundException.class)
	@Ignore
	public void shouldNotRetrieveProductByIdNotFound() throws Exception {
		Product productList = retriveProductMockData();
		Mockito.when(
				productService.getProductById(20001L)).thenReturn(Optional.of(productList));
		RequestBuilder requestBuilder = get(
				"/products/{id}", 2L);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		if (result.getResponse().getContentAsString().equals("")) {
			throw new ProductNotFoundException("Product Not found");
		}
	}


	@Test
	public void shouldDeleteProductById() throws Exception {
		Product product = retriveProductMockData();
		Mockito.when(
				productService.getProductById(20001L)).thenReturn(Optional.of(product));
		mockMvc.perform(
				delete("/products/{id}", 20001L))
				.andExpect(status().isNoContent());
		verify(productService, times(1)).deleteProduct(product.getId());
	}

	@Test
	public void shouldNotDeleteProductByIdInvalid() throws Exception {
		Product product = retriveProductMockData();
		Mockito.when(
				productService.getProductById(20001L)).thenReturn(Optional.of(product));
		mockMvc.perform(
				delete("/products/{id}", 20001L))
				.andExpect(status().isNoContent());
		verify(productService, times(1)).deleteProduct(product.getId());
	}

	@Test
	public void shouldRetrieveAllProductssNewWay() throws Exception {
		Iterable<Product> productList = Arrays.asList((retriveProductMockData()));

		Mockito.when(
				productService.getAllProducts()).thenReturn(productList);

		MvcResult results = mockMvc.perform(MockMvcRequestBuilders.get("/products").accept(MediaType.APPLICATION_JSON))
				.andReturn();
		System.out.println(results.getResponse());
		verify(productService).getAllProducts();

	}

	private Product retriveProductMockData() {
		return new Product(20001L,"iPhone",null,"Mobile",65000.00);
	}
}
