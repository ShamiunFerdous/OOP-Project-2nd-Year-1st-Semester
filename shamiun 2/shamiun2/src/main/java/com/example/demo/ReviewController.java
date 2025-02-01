package com.example.demo;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

import java.net.URL;
import java.util.ResourceBundle;

public class ReviewController implements Initializable {

    @FXML
    private Label cust_name;

    @FXML
    private TextArea review_area;

    public void setData(ReviewData review) {
        cust_name.setText(review.getUsername());
        review_area.setText(review.getReviewText());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}