package com.example.demo;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CustomerController {

    @FXML
    private PasswordField confirm_password;

    @FXML
    private PasswordField customer_password;

    @FXML
    private TextField customer_username;

    @FXML
    private TextField email;

    @FXML
    private AnchorPane form;

    @FXML
    private TextField gender;

    @FXML
    private Button loginBtn;

    @FXML
    private AnchorPane login_form;

    @FXML
    private Hyperlink login_hyperlink;

    @FXML
    private TextField name;

    @FXML
    private Button registerBtn;

    @FXML
    private AnchorPane register_form;

    @FXML
    private Hyperlink register_hyperLink;

    @FXML
    private PasswordField register_password;

    @FXML
    private TextField register_username;

    @FXML
    private ImageView someImageView1;

    @FXML
    private ImageView someImageView2;

    private Connection connection;
    private PreparedStatement prepare;
    private ResultSet result;


    public void register() {
        String customerData = "INSERT INTO customer (name, email, gender, username, password, confirm_password) VALUES (?, ?, ?, ?, ?, ?)";
        connection = Database.connnectionDB();

        try {
            Alert alert;

            if (register_username.getText().isEmpty() || register_password.getText().isEmpty() || email.getText().isEmpty() || name.getText().isEmpty() || gender.getText().isEmpty() || confirm_password.getText().isEmpty()) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Register Failed");
                alert.setHeaderText(null);
                alert.setContentText("Please fill up the form");
                alert.showAndWait();
                return;
            }

            // Email validation
            if (!isValidEmail(email.getText())) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Invalid Email");
                alert.setHeaderText(null);
                alert.setContentText("Please enter a valid email address.");
                alert.showAndWait();
                return;
            }

            if (connection == null) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Connection Error");
                alert.setHeaderText(null);
                alert.setContentText("Unable to connect to the database.");
                alert.showAndWait();
                return;
            }

            if (!register_password.getText().equals(confirm_password.getText())) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Sign Up Failed");
                alert.setHeaderText(null);
                alert.setContentText("Passwords do not match.");
                alert.showAndWait();
                return;
            }

            prepare = connection.prepareStatement(customerData);
            prepare.setString(1, name.getText());
            prepare.setString(2, email.getText());
            prepare.setString(3, gender.getText());
            prepare.setString(4, register_username.getText());
            prepare.setString(5, register_password.getText());
            prepare.setString(6, confirm_password.getText());
            int result = prepare.executeUpdate();

            if (result > 0) {
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Sign Up Success");
                alert.setHeaderText(null);
                alert.setContentText("Customer account created successfully!");
                alert.showAndWait();
            } else {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Sign Up Failed");
                alert.setHeaderText(null);
                alert.setContentText("Failed to create account.");
                alert.showAndWait();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isValidEmail(String email) {
        // Regex pattern for validating email
        String emailRegex = "^[A-Za-z0-9+_.-]+@gmail\\.com$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public void login(){

        String loginData = "SELECT * FROM customer WHERE username=? AND password=?";
        connection = Database.connnectionDB();

        try{

            Alert alert;

            if (customer_username.getText().isEmpty() || customer_password.getText().isEmpty()) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Login Failed");
                alert.setHeaderText(null);
                alert.setContentText("Invalid Username or Password!");
                alert.showAndWait();
                return;
            }

            if (connection == null) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Connection Error");
                alert.setHeaderText(null);
                alert.setContentText("Unable to connect to the database.");
                alert.showAndWait();
                return;
            }

            prepare = connection.prepareStatement(loginData);
            prepare.setString(1, customer_username.getText());
            prepare.setString(2, customer_password.getText());
            ResultSet result = prepare.executeQuery();

            if (result.next()) {
                loginBtn.getScene().getWindow().hide();
                Parent root = FXMLLoader.load(getClass().getResource("custdash-view.fxml"));
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.show();
            } else {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Login Failed");
                alert.setHeaderText(null);
                alert.setContentText("Invalid Username or Password!");
                alert.showAndWait();
            }



        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void Linkswap(javafx.event.ActionEvent event) throws IOException {
        if (event.getSource() == register_hyperLink) {
            register_form.setVisible(false);
            login_form.setVisible(true);
        } else if (event.getSource() == login_hyperlink) {
            login_form.setVisible(false);
            register_form.setVisible(true);
        }
    }
}


