package com.isaya.ecom_backend.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.isaya.ecom_backend.model.Product;
import com.isaya.ecom_backend.service.ProductService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;



@RestController
@CrossOrigin
@RequestMapping("/api")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/products")
    public ResponseEntity<List<Product>> getAllProducts() {
        return new ResponseEntity<>( productService.getAllProducts(), HttpStatus.OK);
    }
    
    
    @GetMapping("/product/{id}")
    public ResponseEntity<Product> getProductDetails(@PathVariable int id) {
        Product product = productService.getProductByid(id);
        if (product == null) 
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        else
        return new ResponseEntity<>( product, HttpStatus.OK);
    
    }
    
}
