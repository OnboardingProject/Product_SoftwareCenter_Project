package com.ust.ProjectSoftwareCenter.Controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ust.ProjectSoftwareCenter.Constant.SoftwareCenterConstant;
import com.ust.ProjectSoftwareCenter.Exception.SoftwareCenterException;
import com.ust.ProjectSoftwareCenter.Exception.SoftwareCenterProductException;
import com.ust.ProjectSoftwareCenter.Model.Product;
import com.ust.ProjectSoftwareCenter.Request.ProductRequest;
import com.ust.ProjectSoftwareCenter.Response.ProductResponsePayload;
import com.ust.ProjectSoftwareCenter.Service.SoftwareCenterService;

import io.swagger.v3.oas.annotations.Operation;

import org.springframework.web.bind.annotation.RequestBody;
import lombok.extern.slf4j.Slf4j;
/*
* @Author : Deepa G K/ Juby Johnson/ Sheeba V R
* @Description : Controller class for Product
* @Date : 20.09.2022
*/

@Slf4j
@RestController
@Validated
@RequestMapping("api/SoftwareCenter")
public class SoftwareCenterController {

	@Autowired
	SoftwareCenterService softwareCenterService;

	/**
	 * @Description Add new product Api
	 * @param product
	 * @return product
	 * @throws Exception
	 */
	@PostMapping("/AddNewProduct")
	@Operation(summary = "adding the new product", description = "This Api is used to add new product")
	public ResponseEntity<ProductResponsePayload> addNewProduct(@Valid @RequestBody ProductRequest productRequest) {
		try {
			log.info("Api call for save new product");
			List<Object> newProductList = new ArrayList<Object>();
			Product productSave = softwareCenterService.saveNewProduct(productRequest);
			newProductList.add(productSave);
			return new ResponseEntity<ProductResponsePayload>(
					new ProductResponsePayload(newProductList, SoftwareCenterConstant.SAVE_PRODUCT, ""),
					HttpStatus.CREATED);
		} catch (SoftwareCenterException softwareCenterException) {
			log.error("Product not found Exception");
			return new ResponseEntity<ProductResponsePayload>(
					new ProductResponsePayload(null, "", softwareCenterException.getErrorMessage()),
					HttpStatus.CONFLICT);
		} catch (Exception exception) {
			log.error("Internal Server Error");
			return new ResponseEntity<ProductResponsePayload>(
					new ProductResponsePayload(null, "", exception.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);

		}

	}

	/**
	 *
	 * @param productId
	 * @return status of deleteProduct
	 * @throws softwareCenterException
	 * @throws Exception
	 */
	@DeleteMapping("/{productId}")
	@Operation(summary = "delete product softly", description = "This Api is used to delete products temporarly")
	public ResponseEntity<?> deleteProduct(@PathVariable("productId") String productId) throws Exception {
		try {
			log.info("Controller soft delete product starts with productRequest:{}" + productId);
			Product product = softwareCenterService.deleteProductById(productId);
			log.info("controller softDeleteProduct method ends with http status OK response:{}" + product);
			return new ResponseEntity<>(SoftwareCenterConstant.PRODUCT_DELETED, HttpStatus.OK);
		} catch (SoftwareCenterException softwareCenterException) {
			log.error("Throw exception" + softwareCenterException.getErrorMessage());
			return new ResponseEntity<>(softwareCenterException.getErrorMessage(), HttpStatus.NOT_FOUND);
		} catch (Exception exception) {
			log.error("Internal Server Error");
			return new ResponseEntity<>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

		}
	}

	/**
	 *
	 * @param productName
	 * @return product
	 * @throws softwareCenterException
	 * @throws Exception
	 */
	@GetMapping("/ProductByName/{productName}")
	@Operation(summary = "search product by name", description = "This Api is used to search products by name")
	public ResponseEntity<?> searchByProductName(@PathVariable String productName) throws Exception {
		try {
			log.info("List the product details searched by product name");
			List<Product> productList = softwareCenterService.getProductByName(productName);
			return new ResponseEntity<List<Product>>(productList, HttpStatus.OK);
		} catch (SoftwareCenterProductException softwareCenterProductException) {
			log.error("Product not found exception");
			return new ResponseEntity<String>(softwareCenterProductException.getMsg(), HttpStatus.NOT_FOUND);
		}

		catch (Exception exception) {
			log.error("Internal Server Error");
			return new ResponseEntity<>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
