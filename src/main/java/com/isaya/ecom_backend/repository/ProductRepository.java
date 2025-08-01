package com.isaya.ecom_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.isaya.ecom_backend.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    // This interface will automatically provide CRUD operations for Product entities
    // No additional methods are needed for basic operations

    
}
