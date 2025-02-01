package com.example.demo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class CustdashController {

    @FXML
    private Label User_name;

    @FXML
    private AnchorPane prod_but_form;

    @FXML
    private AnchorPane prod_form;

    @FXML
    private GridPane prod_gridPane;

    @FXML
    private ScrollPane prod_srollPane;

    @FXML
    private Button product_btn;

    @FXML
    private GridPane review_gridPane;

    @FXML
    private ScrollPane review_scrolPane;

    @FXML
    private Button sendBtn;

    @FXML
    private Button user_signOut;

    @FXML
    private AnchorPane write_form;

    @FXML
    private TextArea write_text;

    @FXML
    private Button reviewBtn;

    @FXML
    private Button messenger_customer;

    private Connection connection;
    private PreparedStatement prepare;
    private ResultSet result;

    private ObservableList<ReviewData> reviewList = FXCollections.observableArrayList();

    @FXML
    public void sendReview() {
        String reviewText = write_text.getText();
        if (reviewText.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText(null);
            alert.setContentText("Review text cannot be empty.");
            alert.showAndWait();
            return;
        }

        String sql = "INSERT INTO review (username, review_text) VALUES (?, ?)";
        connection = Database.connnectionDB();

        try {
            prepare = connection.prepareStatement(sql);
            prepare.setString(1, username); // Ensure username is set correctly
            prepare.setString(2, reviewText);
            int rowsAffected = prepare.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Review saved successfully.");
                write_text.clear(); // Clear the text area
                loadReviews(); // Reload and display all reviews
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Failed to save review.");
                alert.showAndWait();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Database Error");
            alert.setHeaderText(null);
            alert.setContentText("An error occurred while saving the review: " + e.getMessage());
            alert.showAndWait();
        }
    }

    public String getUsername() {
        return username; // Return the stored username
    }


    public void loadReviews() {
        reviewList.clear(); // Clear current list
        review_gridPane.getChildren().clear(); // Clear the UI

        String sql = "SELECT * FROM review"; // Load all reviews from the database

        try {
            connection = Database.connnectionDB();
            prepare = connection.prepareStatement(sql);
            result = prepare.executeQuery();

            while (result.next()) {
                String dbUsername = result.getString("username");
                String dbReviewText = result.getString("review_text");
                ReviewData review = new ReviewData(dbUsername, dbReviewText);
                reviewList.add(review);
            }

            System.out.println("Reviews loaded: " + reviewList.size()); // Debug log
            displayReviews(); // Refresh the grid with the updated list
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private void displayReviews() {
        review_gridPane.getChildren().clear(); // Clear existing reviews

        int row = 0;
        for (ReviewData review : reviewList) {
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("review-view.fxml")); // Ensure path is correct
                AnchorPane pane = loader.load();

                // Pass review data to the controller
                ReviewController reviewController = loader.getController();
                reviewController.setData(review);

                // Add pane to the grid
                review_gridPane.add(pane, 0, row);
                GridPane.setMargin(pane, new Insets(10));
                row++;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private String username;
    private ObservableList<ProductData> cartList = FXCollections.observableArrayList();

    public ObservableList<ProductData> getCartList() {
        String sql = "SELECT * FROM product";
        ObservableList<ProductData> list = FXCollections.observableArrayList();
        connection = Database.connnectionDB();

        try {
            prepare = connection.prepareStatement(sql);
            result = prepare.executeQuery();

            while (result.next()) {
                ProductData prod = new ProductData(
                        result.getString("product_id"),
                        result.getString("name"),
                        result.getDouble("price"),
                        result.getString("image")
                );
                list.add(prod);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void display() {
        cartList.clear();
        cartList.addAll(getCartList()); // Fetch product data

        int row = 0;
        int column = 0;

        prod_gridPane.getChildren().clear(); // Clear existing nodes in the GridPane

        for (int i = 0; i < cartList.size(); i++) {
            try {
                FXMLLoader load = new FXMLLoader();
                load.setLocation(getClass().getResource("cart-view.fxml")); // Adjust path if needed
                AnchorPane pane = load.load();

                // Pass product data to the cart-view controller
                CartController controller = load.getController();
                controller.setData(cartList.get(i));

                // Ensure column resets and rows increase
                if (column == 3) {
                    column = 0;
                    row++;
                }

                // Add pane to the grid
                prod_gridPane.add(pane, column, row);

                // Set margins for spacing between panes
                GridPane.setMargin(pane, new javafx.geometry.Insets(10));

                column++;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void setUsername(String username) {
        this.username = username;
        User_name.setText(username);
        loadReviews(); // Load reviews for the user
        display(); // Call display() to show products
    }


    public void Switch(javafx.event.ActionEvent event){
        if(event.getSource() == product_btn){
            prod_form.setVisible(true);
            write_form.setVisible(false);
            display();
            displayReviews();
            loadReviews();
        }
        if(event.getSource() == reviewBtn){
            prod_form.setVisible(false);
            write_form.setVisible(true);
            display();
            displayReviews();
            loadReviews();
        }
    }

    public void OpenClientChat() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("client-view.fxml"));
            Parent root = loader.load();

            // Get the controller and set the username
            ClientController clientController = loader.getController();
            clientController.setUsername(this.username); // Pass the username to the client controller

            Stage stage = (Stage) messenger_customer.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.centerOnScreen();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void signout() {
        try {
            // Create a confirmation alert
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Message");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure, you want to logout?");

            // Show the alert and wait for user response
            Optional<ButtonType> result = alert.showAndWait();

            // If user confirms, redirect to hello-view.fxml
            if (result.isPresent() && result.get() == ButtonType.OK) {
                // Load the FXML file
                FXMLLoader loader = new FXMLLoader(getClass().getResource("customer-view.fxml"));
                Parent root = loader.load();

                // Get current stage and set the new scene
                Stage stage = (Stage) user_signOut.getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.centerOnScreen();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {
        prod_gridPane.setHgap(10); // Horizontal gap between columns
        prod_gridPane.setVgap(10); // Vertical gap between rows
        write_text.setWrapText(true);
        loadReviews();
        display();
    }
}