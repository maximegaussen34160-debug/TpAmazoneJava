package com.example.demo.produit.model;

public class Product {

    Long id;
    String name;
    String descrpit;
    Integer price;
    Integer stock;

public Product(Long id, String name, String descrpit ,Integer price,Integer stock) {
        this.id = id;
        this.name = name;
        this.descrpit = descrpit;
        this.price = price;
        this.stock = stock;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescrpit() {
        return descrpit;
    }

    public Integer getPrice() {
        return price;
    }

    public Integer getStock() {
        return stock;
    }
    
}
