package com.ust.ProjectSoftwareCenter.Response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/*
* @Author : Deepa G K/ Juby Johnson/ Sheeba V R
* @Description : Api response structure for product module
* @Date : 20.09.2022
*/
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponsePayload {

	private List<Object> data;
	private String successMessage;
	private String errorMessage;
}
