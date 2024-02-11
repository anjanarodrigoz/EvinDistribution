package com.example.evindistribution.Models;

import java.math.BigDecimal;

public class ItemDetails {

    String id,name;
    double price;
    int stock;

    public ItemDetails(){

    }

    public ItemDetails(String id, String name, double price, int qty) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = qty;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }
}