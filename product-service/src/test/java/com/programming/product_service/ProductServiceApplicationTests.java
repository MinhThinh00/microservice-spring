package com.programming.product_service;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.programming.product_service.dto.ProductRequest;
import com.programming.product_service.repository.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;


import java.math.BigDecimal;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ProductServiceApplicationTests {

	@Container
	static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:4.4.2");
	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private ProductRepository productRepository;

	static {
		mongoDBContainer.start();
	}

	@DynamicPropertySource
	static void setProperties(DynamicPropertyRegistry dymDynamicPropertyRegistry) {
		dymDynamicPropertyRegistry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
	}

	@Test
	void shouldCreateProduct() throws Exception {
		ProductRequest productRequest = getProductRequest();
		String productRequestString = objectMapper.writeValueAsString(productRequest);
		mockMvc.perform(MockMvcRequestBuilders.post("/api/product")
						.contentType(MediaType.APPLICATION_JSON)
						.content(productRequestString))
				.andExpect(status().isCreated());
		Assertions.assertEquals(1, productRepository.findAll().size());
	}
private ProductRequest getProductRequest() {
	return ProductRequest.builder()
			.name("iPhone 13")
			.description("iPhone 13")
			.price(BigDecimal.valueOf(1200))
			.build();
}

//	@Test
//	void shouldCreateProductFromExcelData() throws Exception {
//		String filePath = "D:\\UTC_Y4\\product_test.xlsx";
//		List<ProductRequest> productRequests = ExcelReader.readProductRequests(filePath);
//
//		for (int i = 0; i < productRequests.size(); i++) {
//			ProductRequest productRequest = productRequests.get(i);
//			String productRequestString = objectMapper.writeValueAsString(productRequest);
//
//			try {
//				mockMvc.perform(MockMvcRequestBuilders.post("/api/product")
//								.contentType(MediaType.APPLICATION_JSON)
//								.content(productRequestString))
//						.andExpect(status().isCreated());
//
//				// Ghi kết quả thành công vào Excel
//				ExcelReader.writeTestResult(filePath, i + 1, "Thành công", "");
//
//			} catch (Exception e) {
//				// Ghi kết quả thất bại vào Excel với thông báo lỗi
//				ExcelReader.writeTestResult(filePath, i + 1, "Không thành công", e.getMessage());
//			}
//		}
//		// Ghi tổng kết vào file Excel
//		ExcelReader.writeTestSummary(filePath);
//		// Kiểm tra tổng số sản phẩm được tạo trong cơ sở dữ liệu
//		Assertions.assertEquals(productRequests.size(), productRepository.findAll().size());
//	}
}