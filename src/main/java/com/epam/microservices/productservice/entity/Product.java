package com.epam.microservices.productservice.entity;


import java.util.Date;

import javax.annotation.Generated;
import javax.persistence.*;
import java.util.List;
import com.epam.microservices.productservice.dto.Review;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name="product")
public class Product {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String name;

	private Date createdDate;

	private String desc;

	private Double price;

	private String category;

	@Transient
	private List<Review> prodReviews;

}
