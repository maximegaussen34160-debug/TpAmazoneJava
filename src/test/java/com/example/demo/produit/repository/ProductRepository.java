package com.example.demo.produit.repository;

import java.util.ArrayList;
import java.util.List;


import org.springframework.stereotype.Repository;

import com.example.demo.produit.model.Product;



@Repository
public class ProductRepository {

    private final List<Product> products = new ArrayList<>(); // final = référence constante

    public ProductRepository() { // constructeur !
        products.add(new Product(1L, "Chocalatine", "u ajksazldzaelhrzejkdhza" , 15 ,10)); // 1L = long, 64bits
        products.add(new Product(2L, "Pain au chocolat", "dakzqdhzefazifugiazefguadu" , 1 ,20)); // en java les types de nombre sont stricts, pas permissifs
        products.add(new Product(3L, "Croissant", "dfzqerazerzqerazerzaer" , 2 ,30));
    }

    
    public Product addNewUser(String name , String descript ,Integer price , Integer stock){
        Long id = System.currentTimeMillis() / 1000;

        Product product = new Product(id ,name, descript , price,stock); 

        this.products.add(product);

        return product;
    }


    public List<Product> findAll() {
        return products;
    }
    
}
