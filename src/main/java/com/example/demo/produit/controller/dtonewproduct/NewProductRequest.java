package com.example.demo.produit.controller.dtonewproduct;

public class NewProductRequest {
    private String name;
    private String descrpit;
    private Integer price;
    private Integer stock;


    public String getName() {
        return name;
    }

    public String getDescript() {
        return descrpit;
    }

    public Integer getPrice() {
        return price;
    }

    public Integer getStock() {
        return stock;
    }
}
