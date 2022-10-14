package com.ust.ProjectSoftwareCenter.Model;

import java.time.LocalDateTime;
import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/*
* @Author : Deepa G K/ Juby Johnson/ Sheeba V R
* @Description : Model class for Product
* @Date : 20.09.2022
*/

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document("Product")
public class Product {
	@Id
	private String productId;
	@NotNull(message = "The product name cannot be empty")
	@NotEmpty(message = "The product name cannot be empty")
	private String productName;
	private String productDescription;
	private float contractSpend;
	private int stakeholder;
	private List<String> categoryLevel;
	private List<String> categoryLevelDescription;
	private String createdBy;
	private LocalDateTime createdTime;
	private String lastUpdatedBy;
	private LocalDateTime lastUpdatedTime;
	private String isDeleted;

	public Product(
			@NotNull(message = "The product name cannot be empty") @NotEmpty(message = "The product name cannot be empty") String productName,
			String productDescription, float contractSpend, int stakeholder, List<String> categoryLevel,
			List<String> categoryLevelDescription, String createdBy, LocalDateTime createdTime, String lastUpdatedBy,
			LocalDateTime lastUpdatedTime, String isDeleted) {
		super();
		this.productName = productName;
		this.productDescription = productDescription;
		this.contractSpend = contractSpend;
		this.stakeholder = stakeholder;
		this.categoryLevel = categoryLevel;
		this.categoryLevelDescription = categoryLevelDescription;
		this.createdBy = createdBy;
		this.createdTime = createdTime;
		this.lastUpdatedBy = lastUpdatedBy;
		this.lastUpdatedTime = lastUpdatedTime;
		this.isDeleted = isDeleted;
	}
}
