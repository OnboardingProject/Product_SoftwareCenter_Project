package com.ust.ProjectSoftwareCenter.Repository;

/**
 * @author Juby Johnson
 * @description : Repository class for accessing Product entity.
 * @date : 20.09.2022
 */
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.ust.ProjectSoftwareCenter.Model.Product;

public interface IProductRepository extends MongoRepository<Product, String> {
	@Query("{productName:{$regex:/?0.*/,$options:\"i\"},isDeleted:{$ne:true}}")
	List<Product> findByProductName(String productName);

}
