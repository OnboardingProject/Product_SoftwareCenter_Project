package com.ust.ProjectSoftwareCenter.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.ust.ProjectSoftwareCenter.Constant.SoftwareCenterConstant;
import com.ust.ProjectSoftwareCenter.Exception.SoftwareCenterException;
import com.ust.ProjectSoftwareCenter.Exception.SoftwareCenterProductException;
import com.ust.ProjectSoftwareCenter.Model.Product;
import com.ust.ProjectSoftwareCenter.Repository.IProductRepository;
import com.ust.ProjectSoftwareCenter.Request.ProductRequest;

import lombok.extern.slf4j.Slf4j;

/*
* @Author : Deepa G K/ Juby Johnson/ Sheeba V R
* @Description : Service class for Product
* @Date : 20.09.2022
*/
@Slf4j
@Service
public class SoftwareCenterService implements ISoftwareCenterService {

	@Autowired
	MongoTemplate mongoTemplate;

	@Autowired
	IProductRepository productRepository;

	/**
	 * @Description Add new product
	 * @param product
	 * @return product
	 * @throws SoftwareCenterException
	 */
	public Product saveNewProduct(ProductRequest productRequest) throws Exception {
		log.info("Started save new product method");
		if (Objects.nonNull(productRequest)) {
			Query query = new Query();
			query.addCriteria(Criteria.where("productName").is(productRequest.getProductName()));
			List<Product> productAlready = mongoTemplate.find(query, Product.class);
			log.info("Check whether the product is exist or not");
			if (CollectionUtils.isEmpty(productAlready)) {
				log.info("Save new product");
				Product product = new Product(productRequest.getProductName(), productRequest.getProductDescription(),
						productRequest.getContractSpend(), productRequest.getStakeholder(),
						productRequest.getCategoryLevel(), productRequest.getCategoryLevelDescription(),
						productRequest.getCreatedBy(), LocalDateTime.now(), productRequest.getLastUpdatedBy(),
						LocalDateTime.now(), "false");

				mongoTemplate.save(product);
				return product;
			} else {
				log.error("Product already exist exception");
				throw new SoftwareCenterException(SoftwareCenterConstant.PRODUCT_ALREADY_EXIST);
			}

		} else {
			log.error("Product request not found");
			throw new SoftwareCenterException(SoftwareCenterConstant.NEW_PRODUCT_NOTFOUND);
		}
	}

	/**
	 *
	 * @param productId
	 * @return message of delete product
	 * @throws ProductNotExistsException
	 * @throws ProductException
	 *
	 */
	public Product deleteProductById(String productId) throws Exception {
		log.info("deleteProduct service starts with request : {}" + productId);
		Query query = new Query();
		query.addCriteria(Criteria.where("productId").is(productId));
		Product existingProduct = mongoTemplate.findOne(query, Product.class);
		log.info("Product fetched from DB : {}" + existingProduct);
		if (!ObjectUtils.isEmpty(existingProduct) && existingProduct.getIsDeleted().equalsIgnoreCase("false")) {
			log.info("service deleteProduct exists and the given deleted status is false");
			existingProduct.setIsDeleted("true");
			return mongoTemplate.save(existingProduct);
		} else if (!ObjectUtils.isEmpty(existingProduct) && existingProduct.getIsDeleted().equalsIgnoreCase("true")) {
			log.error("Service deleteProduct is already deleted");
			throw new SoftwareCenterException(SoftwareCenterConstant.PRODUCT_ALREADY_DELETED);
		} else {

			log.error("Service deleteProduct is not found");
			throw new SoftwareCenterException(SoftwareCenterConstant.PRODUCT_NOT_EXISTS);

		}
	}

	/**
	 *
	 * @param productName
	 * @return product
	 * @throws SoftwareCenterException
	 *
	 */
	@Override
	public List<Product> getProductByName(String productName) throws Exception {
		log.info("Search product start with request:{}", productName);
		List<Product> productNameList = new ArrayList<Product>();

		if (!StringUtils.isEmpty(productName)) {
			log.info("Fetch the product from db:{}");
			productNameList = productRepository.findByProductName(productName);

			if (CollectionUtils.isEmpty(productNameList)) {
				log.error("Product not found in db:{}", productName);
				throw new SoftwareCenterProductException(SoftwareCenterConstant.PRODUCT_NOT_EXISTS);
			}
		} else {
			log.error("Input is Invalid");
			throw new SoftwareCenterProductException(SoftwareCenterConstant.INVALID_INPUT);
		}
		log.info("Search product by name service call end");
		return productNameList;
	}

}
