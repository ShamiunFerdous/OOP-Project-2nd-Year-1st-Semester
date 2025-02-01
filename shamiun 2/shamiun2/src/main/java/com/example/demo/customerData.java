package com.example.demo;

import java.sql.Date;

public class customerData {
    private String customerName;
    private String brand;
    private String productName;
    private int quantity;
    private Date date;
    private Double price;

    // Constructor
    public customerData( String brand, String productName, int quantity, Date date, Double price) {
        // this.customerName = customerName;
        this.brand = brand;
        this.productName = productName;
        this.quantity = quantity;
        this.date = date;
        this.price = price;
    }

    // Getter and Setter for customerName
    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    // Getter and Setter for brand
    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    // Getter and Setter for productName
    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    // Getter and Setter for quantity
    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    // Getter and Setter for date
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    // Getter and Setter for price
    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
