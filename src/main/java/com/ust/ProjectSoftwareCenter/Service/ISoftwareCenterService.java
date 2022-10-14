package com.ust.ProjectSoftwareCenter.Service;

/*
* @Author : Juby Johnson
* @Description : Interface for Service class
* @Date : 20.09.2022
*/
import java.util.List;

import com.ust.ProjectSoftwareCenter.Model.Product;

public interface ISoftwareCenterService {

	List<Product> getProductByName(String productName) throws Exception;

}
