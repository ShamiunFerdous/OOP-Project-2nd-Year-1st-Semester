package com.example.demo;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

public class CartController implements Initializable {

    @FXML
    private AnchorPane prod_form;

    @FXML
    private ImageView prod_imageView;

    @FXML
    private Label prod_name;

    @FXML
    private Label prod_price;

    private ProductData prodData;

    public void setData(ProductData prodData) {
        this.prodData = prodData;

        if (prodData != null) {
            prod_name.setText(prodData.getProductName());
            prod_price.setText(String.valueOf(prodData.getPrice()));

            // Fix image path issue
            String path = "file:" + prodData.getImage();
            System.out.println("Image Path: " + path); // Debugging
            Image image = new Image(path, 194, 153, true, true);
            prod_imageView.setImage(image);

        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Additional initialization (if required)
    }
}
