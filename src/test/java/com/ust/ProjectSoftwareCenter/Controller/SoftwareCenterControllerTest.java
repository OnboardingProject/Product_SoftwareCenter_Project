package com.ust.ProjectSoftwareCenter.Controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ust.ProjectSoftwareCenter.Constant.SoftwareCenterConstant;
import com.ust.ProjectSoftwareCenter.Exception.SoftwareCenterException;
import com.ust.ProjectSoftwareCenter.Exception.SoftwareCenterProductException;
import com.ust.ProjectSoftwareCenter.Model.Product;
import com.ust.ProjectSoftwareCenter.Request.ProductRequest;
import com.ust.ProjectSoftwareCenter.Service.SoftwareCenterService;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class SoftwareCenterControllerTest {
	@InjectMocks
	SoftwareCenterController softwareCenterController;
	@Mock
	SoftwareCenterService softwareCenterService;
	@Mock
	SoftwareCenterConstant softwareCenterConstant;

	private MockMvc mockMvc;
	List<String> categoryLevelList = new ArrayList<String>();
	List<String> categoryLevelDescription = new ArrayList<String>();
	ProductRequest productRequest;
	Product product;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(softwareCenterController).build();

		categoryLevelList.add("1-1-1");
		categoryLevelList.add("1-2-2");
		categoryLevelDescription.add("Internal-Developertool-WorkFlow");
		productRequest = new ProductRequest("Java", "Java Programmimg", 1500.00f, 15, categoryLevelList,
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

	@DisplayName("JUnit test for add new product success scenario")
	@Test
	public void addNewProductSuccessTest() throws Exception {
		when(softwareCenterService.saveNewProduct(any())).thenReturn(product);
		mockMvc.perform(MockMvcRequestBuilders.post("/api/SoftwareCenter/AddNewProduct")
				.contentType(MediaType.APPLICATION_JSON).content(asJsonString(productRequest)))
				.andExpect(MockMvcResultMatchers.status().isCreated());
	}

	@DisplayName("JUnit test for when add new product then already exist scenario ")
	@Test
	public void addNewProductAlreadyExistTest() throws Exception {
		when(softwareCenterService.saveNewProduct(any()))
				.thenThrow(new SoftwareCenterException(SoftwareCenterConstant.PRODUCT_ALREADY_EXIST));

		mockMvc.perform(MockMvcRequestBuilders.post("/api/SoftwareCenter/AddNewProduct")
				.contentType(MediaType.APPLICATION_JSON).content(asJsonString(productRequest)))
				.andExpect(MockMvcResultMatchers.status().isConflict()).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.jsonPath("$.errorMessage")
						.value(SoftwareCenterConstant.PRODUCT_ALREADY_EXIST));

		verify(softwareCenterService, times(1)).saveNewProduct(any());

	}

	@DisplayName("JUnit test for  add new product failure scenario ")
	@Test
	public void addNewProductFailureTest() throws Exception {
		when(softwareCenterService.saveNewProduct(any()))
				.thenThrow(new SoftwareCenterException(SoftwareCenterConstant.NEW_PRODUCT_NOTFOUND));

		mockMvc.perform(MockMvcRequestBuilders.post("/api/SoftwareCenter/AddNewProduct")
				.contentType(MediaType.APPLICATION_JSON).content(asJsonString(productRequest)))
				.andExpect(MockMvcResultMatchers.status().isConflict()).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.jsonPath("$.errorMessage")
						.value(SoftwareCenterConstant.NEW_PRODUCT_NOTFOUND));

		verify(softwareCenterService, times(1)).saveNewProduct(any());
	}

	@DisplayName("JUnit test for add new product internal server error scenario ")
	@Test
	public void addNewProductInternalServerFailuerTest() throws Exception {
		when(softwareCenterService.saveNewProduct(any()))
				.thenThrow(new Exception(SoftwareCenterConstant.INTERNALSERVERERROR));

		mockMvc.perform(MockMvcRequestBuilders.post("/api/SoftwareCenter/AddNewProduct")
				.contentType(MediaType.APPLICATION_JSON).content(asJsonString(productRequest)))
				.andExpect(status().isInternalServerError()).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.jsonPath("$.errorMessage")
						.value(SoftwareCenterConstant.INTERNALSERVERERROR));
		verify(softwareCenterService, times(1)).saveNewProduct(any());
	}

	@DisplayName("JUnit test for delete the product success scenario ")
	@Test
	public void givenProductToDeleteShouldReturnDeleteProduct() throws Exception {
		String productId = "p123";
		List<String> categoryLevel = new ArrayList<String>();
		categoryLevel.add("1-1-1");
		categoryLevel.add("1-2-3");

		List<String> categoryLevelDescription = new ArrayList<String>();
		categoryLevelDescription.add("internal-developmenttool-workflow");

		Product product1 = new Product("p123", "Azure Tools", "Azure Tools", 1299.99F, 4, categoryLevel,
				categoryLevelDescription, "u212", LocalDateTime.now(), "u212", LocalDateTime.now(), "true");
		when(softwareCenterService.deleteProductById(productId)).thenReturn(product1);
		mockMvc.perform(
				MockMvcRequestBuilders.delete("/api/SoftwareCenter/p123").contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk());

	}

	@DisplayName("JUnit test for delete the product failure scenario ")
	@Test
	public void givenProductToDeleteShouldReturnStatus() throws Exception {
		when(softwareCenterService.deleteProductById(any()))
				.thenThrow(new SoftwareCenterException(SoftwareCenterConstant.PRODUCT_NOT_EXISTS));

		mockMvc.perform(
				MockMvcRequestBuilders.delete("/api/SoftwareCenter/p123").contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isNotFound());

		verify(softwareCenterService, times(1)).deleteProductById(any());

	}

	@DisplayName("JUnit test for delete the product failure scenario ")
	@Test
	public void notProductFoundToDeleteShouldReturnStatus() throws Exception {
		when(softwareCenterService.deleteProductById(any()))
				.thenThrow(new SoftwareCenterException(SoftwareCenterConstant.PRODUCT_ALREADY_DELETED));
		mockMvc.perform(
				MockMvcRequestBuilders.delete("/api/SoftwareCenter/p123").contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isNotFound());

		verify(softwareCenterService, times(1)).deleteProductById(any());

	}

	@DisplayName("JUnit test for delete product internal server error scenario ")
	@Test
	public void deleteProductInternalServerFailuerTest() throws Exception {
		when(softwareCenterService.deleteProductById(any()))
				.thenThrow(new Exception(SoftwareCenterConstant.INTERNALSERVERERROR));

		mockMvc.perform(
				MockMvcRequestBuilders.delete("/api/SoftwareCenter/p123").contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isInternalServerError());
		verify(softwareCenterService, times(1)).deleteProductById(any());

	}

	@DisplayName("JUnit test for search product by name success scenario")
	@Test
	public void testSearchByProductName() throws JsonProcessingException, Exception {

		List<Product> productSearchList = getProduct();
		when(softwareCenterService.getProductByName(Mockito.any())).thenReturn(productSearchList);
		mockMvc.perform(MockMvcRequestBuilders.get("/api/SoftwareCenter/ProductByName/{productName}", "Television")
				.contentType(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isOk());
	}

	@DisplayName("JUnit test for search product by name failure scenario")
	@Test
	public void testSearchByProductNameEmptyResponse() throws JsonProcessingException, Exception {

		when(softwareCenterService.getProductByName(Mockito.any()))
				.thenThrow(new SoftwareCenterProductException(SoftwareCenterConstant.PRODUCT_NOT_EXISTS));
		mockMvc.perform(MockMvcRequestBuilders.get("/api/SoftwareCenter/ProductByName/{productName}", "Mobile")
				.contentType(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isNotFound());
		verify(softwareCenterService, times(1)).getProductByName(any());

	}

	@DisplayName("JUnit test for search product by name internal sever error scenario")
	@Test
	public void testSearchProductInternalServerFailuer() throws JsonProcessingException, Exception {
		when(softwareCenterService.getProductByName(any()))
				.thenThrow(new Exception(SoftwareCenterConstant.INTERNALSERVERERROR));

		mockMvc.perform(MockMvcRequestBuilders.get("/api/SoftwareCenter/ProductByName/Television")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isInternalServerError());
		verify(softwareCenterService, times(1)).getProductByName(any());

	}

	public static String asJsonString(final Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}
}
