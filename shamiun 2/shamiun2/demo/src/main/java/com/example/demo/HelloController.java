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

public class HelloController {

    @FXML
    private AnchorPane admin_form;

    @FXML
    private Hyperlink admin_hyperLink;

    @FXML
    private Button admin_loginBtn;

    @FXML
    private PasswordField admin_password;

    @FXML
    private TextField admin_username;

    @FXML
    private AnchorPane employee_form;

    @FXML
    private Hyperlink employee_hyperlink;

    @FXML
    private Button employee_loginBtn;

    @FXML
    private PasswordField employee_password;

    @FXML
    private TextField employee_username;

    @FXML
    private ImageView someImageView1;

    @FXML
    private ImageView someImageView2;

    @FXML
    private Hyperlink customer_hyperLink;

    private Connection connection;
    private PreparedStatement prepare;
    private ResultSet result;


    public void employeeLogin(){
         String employeeData = "SELECT * FROM employee WHERE username = ? AND password = ?";
         connection = Database.connnectionDB();

         try{
             Alert alert;

             if(employee_username.getText().isEmpty() || employee_password.getText().isEmpty()){
                 alert = new Alert(Alert.AlertType.ERROR);
                 alert.setTitle("Login failed");
                 alert.setHeaderText(null);
                 alert.setContentText("Invalid Username or Password!");
                 alert.showAndWait();
                 return;
             }

             if(connection == null){
                 alert = new Alert(Alert.AlertType.ERROR);
                 alert.setTitle("Connection Error");
                 alert.setHeaderText(null);
                 alert.setContentText("Unable to connect to the database.");
                 alert.showAndWait();
                 return;
             }

             prepare = connection.prepareStatement(employeeData);
             prepare.setString(1, employee_username.getText());
             prepare.setString(2, employee_password.getText());
             result = prepare.executeQuery();

             if(result.next()){
                 admin_loginBtn.getScene().getWindow().hide();
                 Parent root = FXMLLoader.load(getClass().getResource("emdashboard-view.fxml"));
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

    public void adminLogin() {
        String adminData = "SELECT * FROM admin WHERE username = ? AND password = ?";
        connection = Database.connnectionDB();

        try {
            Alert alert;

            if (admin_username.getText().isEmpty() || admin_password.getText().isEmpty()) {
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

            prepare = connection.prepareStatement(adminData);
            prepare.setString(1, admin_username.getText());
            prepare.setString(2, admin_password.getText());
            result = prepare.executeQuery();

            if (result.next()) {

                admin_loginBtn.getScene().getWindow().hide();
                Parent root = FXMLLoader.load(getClass().getResource("dashboard-view.fxml"));
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


    public void linkswap(javafx.event.ActionEvent event) throws IOException {
        if (event.getSource() == admin_hyperLink) {
            admin_form.setVisible(false);
            employee_form.setVisible(true);
        } else if (event.getSource() == employee_hyperlink) {
            admin_form.setVisible(true);
            employee_form.setVisible(false);
        }
        else if(event.getSource() == customer_hyperLink){
            // Hide the current window (Stage)
            Stage currentStage = (Stage) customer_hyperLink.getScene().getWindow();
            currentStage.hide();

            // Load the customer view
            Parent root = FXMLLoader.load(getClass().getResource("customer-view.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        }
    }
}
