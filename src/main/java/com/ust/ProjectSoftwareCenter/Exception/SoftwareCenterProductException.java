package com.ust.ProjectSoftwareCenter.Exception;

/**
 * @Author : Deepa G K/ Juby Johnson/ Sheeba V R
 * @Description : Exception class for Software Center Project
 * @Date : 20.09.2022
 */

public class SoftwareCenterProductException extends RuntimeException {
	String msg;

	public SoftwareCenterProductException(String msg) {
		super();
		this.msg = msg;
	}

	public String getMsg() {
		return msg;
	}

}
