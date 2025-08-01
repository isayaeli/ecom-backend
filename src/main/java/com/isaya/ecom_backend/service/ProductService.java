package com.isaya.ecom_backend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.isaya.ecom_backend.model.Product;
import com.isaya.ecom_backend.repository.ProductRepository;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductByid(int id) {
        return productRepository.findById(id).orElse(null);
    }
    
}
