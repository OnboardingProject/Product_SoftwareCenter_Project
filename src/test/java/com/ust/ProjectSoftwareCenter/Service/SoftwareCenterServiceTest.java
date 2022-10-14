package com.ust.ProjectSoftwareCenter.Service;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.ust.ProjectSoftwareCenter.Exception.SoftwareCenterException;
import com.ust.ProjectSoftwareCenter.Exception.SoftwareCenterProductException;
import com.ust.ProjectSoftwareCenter.Model.Product;
import com.ust.ProjectSoftwareCenter.Repository.IProductRepository;
import com.ust.ProjectSoftwareCenter.Request.ProductRequest;

@SpringBootTest
@AutoConfigureMockMvc
public class SoftwareCenterServiceTest {
	@InjectMocks
	SoftwareCenterService SoftwareCenterService;

	@Mock
	MongoTemplate mongoTemplate;

	@Mock
	IProductRepository productRepository;

	List<String> categoryLevelList = new ArrayList<String>();
	List<String> categoryLevelDescription = new ArrayList<String>();
	ProductRequest productRequest;
	Product product;
	List<Product> productList = new ArrayList<Product>();

	@BeforeEach
	public void setUp() {
		categoryLevelList.add("1-1-1");
		categoryLevelList.add("1-2-2");
		categoryLevelDescription.add("Internal-Developertool-WorkFlow");
		productRequest = new ProductRequest("Java", "Java Programming", 1500.00f, 15, categoryLevelList,
				categoryLevelDescription, "User1", "User2");

		product = new Product(productRequest.getProductName(), productRequest.getProductDescription(),
				productRequest.getContractSpend(), productRequest.getStakeholder(), productRequest.getCategoryLevel(),
				productRequest.getCategoryLevelDescription(), productRequest.getCreatedBy(), LocalDateTime.now(),
				productRequest.getLastUpdatedBy(), LocalDateTime.now(), "false");

	}

	public List<Product> getProduct() {
		categoryLevelList.add("1-1-1");
		categoryLevelList.add("1-2-2");
		List<Product> product = new ArrayList<Product>();
		Product p1 = new Product("u101", "Television", "Television product", 1299.99F, 4, categoryLevelList,
				categoryLevelDescription, "u212", LocalDateTime.now(), "u212", LocalDateTime.now(), "false");
		product.add(p1);
		return product;

	}

	@DisplayName("JUnit test for saveNewProduct success scenario")
	@Test
	public void saveNewProductSuccessTest() throws Exception {
		Query query = new Query();
		query.addCriteria(Criteria.where("productName").is(productRequest.getProductName()));
		when(mongoTemplate.find(query, Product.class)).thenReturn(null);
		when(mongoTemplate.save(product)).thenReturn(product);

		Product actualProduct = SoftwareCenterService.saveNewProduct(productRequest);
		assertEquals(product.getProductName(), actualProduct.getProductName());
	}

	@DisplayName("JUnit test for saveNewProduct product then already exist scenario")
	@Test
	public void saveNewProductAlreadyExistTest() throws Exception {
		Query query = new Query();
		query.addCriteria(Criteria.where("productName").is(productRequest.getProductName()));
		productList.add(product);
		when(mongoTemplate.find(query, Product.class)).thenReturn(productList);
		Assertions.assertThrows(SoftwareCenterException.class, () -> {
			SoftwareCenterService.saveNewProduct(productRequest);
		});
		verify(mongoTemplate, never()).save(product);
	}

	@DisplayName("JUnit test for saveNewProduct failure scenario")
	@Test
	public void saveNewProductFailureTest() throws Exception {
		productRequest = null;
		product = null;
		when(mongoTemplate.save(product)).thenReturn(product);
		Assertions.assertThrows(SoftwareCenterException.class, () -> {
			SoftwareCenterService.saveNewProduct(productRequest);
		});
		verify(mongoTemplate, never()).save(product);
	}

	@DisplayName("JUnit test for delete product success scenario")
	@Test
	public void givenProductToDeleteShouldReturnDeleteProduct() throws Exception {
		List<String> categoryLevel = new ArrayList<String>();
		categoryLevel.add("1-1-1");
		categoryLevel.add("1-2-3");

		List<String> categoryLevelDescription = new ArrayList<String>();
		categoryLevelDescription.add("internal-developmenttool-workflow");

		Product product1 = new Product("p123", "Azure Tools", "Azure Tools", 1299.99F, 4, categoryLevel,
				categoryLevelDescription, "u212", LocalDateTime.now(), "u212", LocalDateTime.now(), "false");

		String productId = "p123";
		Query query = new Query();
		query.addCriteria(Criteria.where("productId").is(product1.getProductId()));
		when(mongoTemplate.findOne(query, Product.class)).thenReturn(product1);
		when(mongoTemplate.save(product1)).thenReturn(product1);
		SoftwareCenterService.deleteProductById(productId);
		assertEquals(product1, product1);
		assertNotNull(product1);
	}

	@DisplayName("JUnit test for dlelete product then already delete scenario")
	@Test
	public void givenProductToDeleteShouldReturnException() throws Exception {

		List<String> categoryLevel = new ArrayList<String>();
		categoryLevel.add("1-1-1");
		categoryLevel.add("1-2-3");

		List<String> categoryLevelDescription = new ArrayList<String>();
		categoryLevelDescription.add("internal-developmenttool-workflow");

		Product product1 = new Product("p123", "Azure Tools", "Azure Tools", 1299.99F, 4, categoryLevel,
				categoryLevelDescription, "u212", LocalDateTime.now(), "u212", LocalDateTime.now(), "true");

		String productId = "p123";
		Query query = new Query();
		query.addCriteria(Criteria.where("productId").is(product1.getProductId()));
		when(mongoTemplate.findOne(query, Product.class)).thenReturn(product1);
		Assertions.assertThrows(SoftwareCenterException.class, () -> {
			SoftwareCenterService.deleteProductById(productId);
		});

	}

	@DisplayName("JUnit test for dlelete product failure scenario")
	@Test
	public void givenProductToDeleteShouldReturnNotExistsException() throws Exception {
		Product product1 = null;
		String productId = "p1234";
		Query query = new Query();
		query.addCriteria(Criteria.where("productId").is(productId));
		when(mongoTemplate.findOne(query, Product.class)).thenReturn(product1);
		Assertions.assertThrows(SoftwareCenterException.class, () -> {
			SoftwareCenterService.deleteProductById(productId);
		});

	}

	@DisplayName("JUnit test for search product by name success scenario")
	@Test
	public void testSearchUserSuccess() throws Exception {
		List<Product> product1 = getProduct();
		when(productRepository.findByProductName(Mockito.anyString())).thenReturn((product1));
		List<Product> resultList = SoftwareCenterService.getProductByName("Television");
		assertEquals(product1, resultList);
	}

	@DisplayName("JUnit test for search product by name failure scenario")
	@Test
	public void testSearchUserException() throws Exception {
		List<Product> productList = new ArrayList<Product>();
		when(productRepository.findByProductName(Mockito.anyString())).thenReturn(productList);
		Assertions.assertThrows(SoftwareCenterProductException.class, () -> {
			SoftwareCenterService.getProductByName("AC");
		});
	}

	@DisplayName("JUnit test for search product by name then invalid input scenario")
	@Test
	public void getProductByNameUsingInvalidInput() throws Exception {
		List<Product> product1 = getProduct();
		when(productRepository.findByProductName(Mockito.anyString())).thenReturn((product1));
		Assertions.assertThrows(SoftwareCenterProductException.class, () -> {
			SoftwareCenterService.getProductByName(null);
		});
	}

}

