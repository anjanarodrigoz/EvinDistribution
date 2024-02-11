package com.example.evindistribution.Models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SaleDetails implements Serializable {

    String saleId;
    List<String> itemId,itemName;
    List<Integer> quantity;
    List<Double> itemPrice,salePrice;


    public SaleDetails(){

    }

    public SaleDetails(String saleId, List<String> itemId, List<String> itemName, List<Integer> quantity, List<Double> itemPrice, List<Double> salePrice) {
        this.saleId = saleId;
        this.itemId = itemId;
        this.itemName = itemName;
        this.quantity = quantity;
        this.itemPrice = itemPrice;
        this.salePrice = salePrice;


    }



    public String getSaleId() {
        return saleId;
    }

    public void setSaleId(String saleId) {
        this.saleId = saleId;
    }

    public List<String> getItemId() {
        return itemId;
    }

    public void setItemId(List<String> itemId) {
        this.itemId = itemId;
    }

    public List<String> getItemName() {
        return itemName;
    }

    public void setItemName(List<String> itemName) {
        this.itemName = itemName;
    }

    public List<Integer> getQuantity() {
        return quantity;
    }

    public void setQuantity(List<Integer> quantity) {
        this.quantity = quantity;
    }

    public List<Double> getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(List<Double> itemPrice) {
        this.itemPrice = itemPrice;
    }

    public List<Double> getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(List<Double> salePrice) {
        this.salePrice = salePrice;
    }



    public double getTotal(){

        double total = 0;
        for(double a : salePrice){

            total+=a;
        }

        return total;
    }

    public double getProfit(double total) {

        double cost = 0;
        int index = 0;
        for (double a : itemPrice){
            int b = quantity.get(index);
            cost += (a*b);
            index+=1;
        }
        return total - cost;
    }
}
