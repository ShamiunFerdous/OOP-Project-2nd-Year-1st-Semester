package com.example.demo;

public class ProductData {
    private String productID;
    private String productName;
    private String brand;
    private double price;
    private String status;
    private String image;

    public ProductData(String productID, String productName, String brand, double price, String status, String image) {
        this.productID = productID;
        this.productName = productName;
        this.brand = brand;
        this.price = price;
        this.status = status;
        this.image = image;
    }

    public ProductData(String productID,String productName, double price, String image) {
        this.productID = productID;
        this.productName = productName;
        this.price = price;
        this.image = image;
    }

    // Getter methods
    public String getProductID() {
        return productID;
    }

    public String getProductName() {
        return productName;
    }

    public String getBrand() {
        return brand;
    }

    public double getPrice() {
        return price;
    }

    public String getStatus() {
        return status;
    }
    public String getImage(){
        return image;
    }
}
