package com.example.demo.produit.service;

import com.example.demo.produit.model.Product;
import com.example.demo.produit.repository.ProductRepository;

import java.util.List;


import org.springframework.stereotype.Service;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getProducts() {
        return productRepository.findAll();
    }


    public Product addNewProduct(String name , String descript ,Integer price , Integer stock){

        return productRepository.addNewProduct(name , descript , price , stock);
    
    }
    
    


 
}
