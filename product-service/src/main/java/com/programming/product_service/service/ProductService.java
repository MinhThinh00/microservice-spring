package com.programming.product_service.service;


import com.programming.product_service.dto.ProductRequest;
import com.programming.product_service.dto.ProductResponse;
import com.programming.product_service.model.Product;
import com.programming.product_service.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public void createProduct(ProductRequest productRequest) {
        if (productRequest.getPrice().compareTo(BigDecimal.ONE) <= 0) {
            throw new IllegalArgumentException("gia san pham phai lon hon 1 ");
        }
        if(productRequest.getName().length()<=5 || productRequest.getName().isBlank()){
            throw new IllegalArgumentException("Ten san pham khong hop le");
        }
        if (productRepository.existsByName(productRequest.getName())) {
            throw new IllegalArgumentException("Ten san pham da ton tai ");
        }
        Product product = Product.builder()
                .name(productRequest.getName())
                .description(productRequest.getDescription())
                .price(productRequest.getPrice())
                .build();

        productRepository.save(product);
        log.info("Product {} is saved", product.getId());
    }

    public List<ProductResponse> getAllProducts() {
        List<Product> products = productRepository.findAll();

        return products.stream().map(this::mapToProductResponse).toList();
    }

    private ProductResponse mapToProductResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .build();
    }
}
