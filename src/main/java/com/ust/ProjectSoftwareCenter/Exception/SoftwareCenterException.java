package com.ust.ProjectSoftwareCenter.Exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @Author : Deepa G K/ Juby Johnson/ Sheeba V R
 * @Description : Exception class for Software Center Project
 * @Date : 20.09.2022
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SoftwareCenterException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	private String errorMessage;
}