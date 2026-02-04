package com.example.demo.produit.controller;

import java.util.List;

import com.example.demo.produit.controller.dtonewproduct.NewProductRequest;
import com.example.demo.produit.model.Product;
import com.example.demo.produit.service.ProductService;

import jakarta.validation.Valid;


import org.springframework.web.bind.annotation.*;



public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/products")
    public List<Product> getProducts() {
        return productService.getProducts();
    }

    @PostMapping("/login/newProduct")
    public void postAddNewProduct(@Valid @RequestBody NewProductRequest request) {
        productService.addNewProduct(request.getName(), request.getDescript() , request.getPrice() , request.getStock());

    }
    
}
