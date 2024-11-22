package com.programming.product_service.repository;

import com.programming.product_service.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductRepository extends MongoRepository<Product, String> {
    boolean existsByName(String name);
}
