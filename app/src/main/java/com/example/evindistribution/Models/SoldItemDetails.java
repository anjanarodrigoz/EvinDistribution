package com.example.evindistribution.Models;

public class SoldItemDetails {

    String id,name;
    int qty;
    double itemPrice,cost,sellPrice,profit;

    public SoldItemDetails(){

    }

    public SoldItemDetails(String id, String name, int qty, double itemPrice, double cost, double sellPrice, double profit) {
        this.id = id;
        this.name = name;
        this.qty = qty;
        this.itemPrice = itemPrice;
        this.cost = cost;
        this.sellPrice = sellPrice;
        this.profit = profit;
    }

    public SoldItemDetails(String id, String name, int qty, double itemPrice, double sellPrice, double profit) {
        this.id = id;
        this.name = name;
        this.qty = qty;
        this.itemPrice = itemPrice;
        this.sellPrice = sellPrice;
        this.profit = profit;
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

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public double getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(double itemPrice) {
        this.itemPrice = itemPrice;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public double getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(double sellPrice) {
        this.sellPrice = sellPrice;
    }

    public double getProfit() {
        return profit;
    }

    public void setProfit(double profit) {
        this.profit = profit;
    }
}
