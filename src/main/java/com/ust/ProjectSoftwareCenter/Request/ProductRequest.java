package com.ust.ProjectSoftwareCenter.Request;

/*
* @Author : Sheeba V R
* @Description : Request class for save product tasks
* @Date : 20.09.2022
*/
import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequest {
	@NotNull(message = "The product name cannot be empty")
	@NotEmpty(message = "The product name cannot be empty")
	private String productName;
	private String productDescription;
	private float contractSpend;
	private int stakeholder;
	private List<String> categoryLevel;
	private List<String> categoryLevelDescription;
	private String createdBy;
	private String lastUpdatedBy;

}
