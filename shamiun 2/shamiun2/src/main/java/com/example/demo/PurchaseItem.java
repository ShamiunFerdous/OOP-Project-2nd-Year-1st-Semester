package com.example.demo;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;


public class PurchaseItem {
    private final SimpleStringProperty productName;
    private final SimpleStringProperty brand;
    private final SimpleIntegerProperty quantity;
    private final SimpleDoubleProperty price;

    public PurchaseItem(String productName, String brand, int quantity, double price) {
        this.productName = new SimpleStringProperty(productName);
        this.brand = new SimpleStringProperty(brand);
        this.quantity = new SimpleIntegerProperty(quantity);
        this.price = new SimpleDoubleProperty(price);
    }

    public SimpleStringProperty productNameProperty() {
        return productName;
    }

    public SimpleStringProperty brandProperty() {
        return brand;
    }

    public SimpleIntegerProperty quantityProperty() {
        return quantity;
    }

    public SimpleDoubleProperty priceProperty() {
        return price;
    }

    // Getter for price
    public double getPrice() {
        return price.get(); // Return the value of the SimpleDoubleProperty
    }
}