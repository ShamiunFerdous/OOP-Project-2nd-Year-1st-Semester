package com.example.demo;

import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.collections.ObservableList;
import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;


public class DashboardController {

    @FXML
    private AreaChart<?, ?> income_chart;

    @FXML
    private AnchorPane income_form;

    @FXML
    private Label total_income;

    @FXML
    private Button Income_btn;

    @FXML
    private GridPane reviewGridPane;

    @FXML
    private AnchorPane main_form;

    @FXML
    private AnchorPane Employee_form;

    @FXML
    private TextField Employee_fullName;

    @FXML
    private ComboBox<String> Employee_gender;

    @FXML
    private PasswordField Employee_password;

    @FXML
    private TextField Employee_username;

    @FXML
    private Button addProduct_btn;

    @FXML
    private AnchorPane admin_dashboard_form;

    @FXML
    private Button close;

    @FXML
    private Label dashboard_activeemployee;

    @FXML
    private Button dashboard_btn;

    @FXML
    private AreaChart<?, ?> dashboard_chart;

    @FXML
    private Label dashboard_incom;

    @FXML
    private Label dashboard_totalIncome;

    @FXML
    private Button employee_btn;

    @FXML
    private Button employee_clearBtn;

    @FXML
    private TableColumn<EmployeeData,String> employee_col_date;

    @FXML
    private TableColumn<EmployeeData,String> employee_col_gender;

    @FXML
    private TableColumn<EmployeeData,String> employee_col_name;

    @FXML
    private TableColumn<EmployeeData,String> employee_col_password;

    @FXML
    private TableColumn<EmployeeData,String> employee_col_username;

    @FXML
    private Button employee_delBtn;

    @FXML
    private Button employee_savebtn;

    @FXML
    private TableView<EmployeeData> employee_table;

    @FXML
    private Button employee_updatebtn;

    @FXML
    private ImageView logout;

    @FXML
    private Button logout_btn;

    @FXML
    private TextField product_Id;

    @FXML
    private TextField product_brand;

    @FXML
    private Button product_clear;

    @FXML
    private TableView<ProductData> product_tableview;

    @FXML
    private TableColumn<ProductData, String> product_col_id;

    @FXML
    private TableColumn<ProductData, String> product_col_name;

    @FXML
    private TableColumn<ProductData, String> product_col_brand;

    @FXML
    private TableColumn<ProductData, String> product_col_price;

    @FXML
    private TableColumn<ProductData, String> product_col_status;

    @FXML
    private Button product_delete;

    @FXML
    private AnchorPane product_form;

    @FXML
    private ImageView product_image;

    @FXML
    private Button product_import;

    @FXML
    private TextField product_name;

    @FXML
    private TextField product_price;

    @FXML
    private TextField product_search;

    @FXML
    private ComboBox<?> product_status;

    @FXML
    private Button product_update;

    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        showDataList();
        productListStatus();
        showEmployeeDataList();
        employeeGenderSelect();
        loadReviews();
        reviewGridPane.setHgap(10);
        reviewGridPane.setVgap(10);
    }

    private ObservableList<ReviewData> reviewList = FXCollections.observableArrayList();

    private void loadReviews() {
        reviewList.clear(); // Clear current list
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

            displayReviews(); // Refresh the UI with the updated list
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void displayReviews() {
        reviewGridPane.getChildren().clear(); // Clear existing reviews

        int row = 1;
        for (ReviewData review : reviewList) {
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("review-view.fxml")); // Ensure path is correct
                AnchorPane pane = loader.load();

                // Pass review data to the controller
                ReviewController reviewController = loader.getController();
                reviewController.setData(review);

                // Add pane to the grid
                reviewGridPane.add(pane, 0, row);
                GridPane.setMargin(pane, new Insets(10)); // Add some margin
                row++;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void close(){
        System.exit(0);
    }

    private Image img;
    // This variable will store the file path

    private Connection connection;
    private PreparedStatement prepare;
    private ResultSet result;
    private Statement statement;

    // For employee
    public ObservableList<EmployeeData> employeeListData(){
        ObservableList<EmployeeData> emList = FXCollections.observableArrayList();

        String sql = "SELECT * FROM employee";
        connection = Database.connnectionDB();

        try{

            EmployeeData em;
            prepare = connection.prepareStatement(sql);
            result = prepare.executeQuery();

            while (result.next()){
                em = new EmployeeData(result.getString("username"),
                        result.getString("password"),
                        result.getString("fullname"),
                        result.getString("gender"),
                        result.getDate("date"));

                emList.add(em);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return emList;
    }

    private ObservableList<EmployeeData> employeeList;
    public void showEmployeeDataList(){
        employeeList = employeeListData();

        employee_col_username.setCellValueFactory(new PropertyValueFactory<>("username"));
        employee_col_password.setCellValueFactory(new PropertyValueFactory<>("password"));
        employee_col_name.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        employee_col_gender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        employee_col_date.setCellValueFactory(new PropertyValueFactory<>("date"));

        employee_table.setItems(employeeList);
    }

    public void EmployeeSelect(){
        EmployeeData em = employee_table.getSelectionModel().getSelectedItem();
        int n = employee_table.getSelectionModel().getSelectedIndex();

        if((n - 1) < -1){
            return;
        }

        Employee_username.setText(em.getUsername());
        Employee_password.setText(em.getPassword());
        Employee_fullName.setText(em.getFullName());
    }

    private String[] genderList = {"Male", "Female"};
    public void employeeGenderSelect(){
        List<String> gen = new ArrayList<>();
        for(String data:genderList){
            gen.add(data);
        }
        ObservableList GEN = FXCollections.observableArrayList(gen);
        Employee_gender.setItems(GEN);
    }

    public void save() {
        Date date = new Date();
        java.sql.Date sqlDate = new java.sql.Date(date.getTime());

        if (Employee_username.getText().isEmpty() ||
                Employee_password.getText().isEmpty() ||
                Employee_fullName.getText().isEmpty() ||
                Employee_gender.getSelectionModel().getSelectedItem() == null) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please fill up the blank field");
            alert.showAndWait(); // Show the alert
        } else {
            String checkUsername = "SELECT username FROM employee WHERE username = ?"; // Fixed typo

            try {
                // Ensure the connection is initialized
                connection = Database.connnectionDB();
                prepare = connection.prepareStatement(checkUsername);
                prepare.setString(1, Employee_username.getText()); // Use prepared statement to prevent SQL injection
                result = prepare.executeQuery();

                if (result.next()) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText(Employee_username.getText() + " already exists");
                    alert.showAndWait(); // Show the alert
                } else {
                    String insertEmdata = "INSERT INTO employee (username, password, fullname, gender, date) VALUES (?, ?, ?, ?, ?)";
                    prepare = connection.prepareStatement(insertEmdata);
                    prepare.setString(1, Employee_username.getText());
                    prepare.setString(2, Employee_password.getText());
                    prepare.setString(3, Employee_fullName.getText());
                    prepare.setString(4, (String) Employee_gender.getSelectionModel().getSelectedItem());
                    prepare.setDate(5, sqlDate); // Use setDate for java.sql.Date

                    prepare.executeUpdate();

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Success");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully Added!");
                    alert.showAndWait(); // Show the alert

                    showEmployeeDataList();
                    reset();
                }
            } catch (Exception e) {
                e.printStackTrace(); // Print the stack trace for debugging
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("An error occurred while saving Employee data: " + e.getMessage());
                alert.showAndWait(); // Show the alert
            } finally {
                // Close resources (if applicable)
                try {
                    if (result != null) result.close();
                    if (prepare != null) prepare.close();
                    if (connection != null) connection.close();
                } catch (Exception e) {
                    e.printStackTrace(); // Handle closing exceptions
                }
            }
        }
    }

    public void reset(){
      Employee_username.setText("");
      Employee_password.setText("");
      Employee_fullName.setText("");
      Employee_gender.getSelectionModel().clearSelection();
    }

    public void EmployeeUpdate() {
        if (Employee_username.getText().isEmpty() ||
                Employee_password.getText().isEmpty() ||
                Employee_fullName.getText().isEmpty() ||
                Employee_gender.getSelectionModel().getSelectedItem() == null) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please fill up the blank field");
            alert.showAndWait();
        } else {
            String updateEmData = "UPDATE employee SET password = ?, fullname = ?, gender = ? WHERE username = ?";

            connection = Database.connnectionDB();

            try {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure you want to update?");
                Optional<ButtonType> option = alert.showAndWait();

                if (option.isPresent() && option.get() == ButtonType.OK) {
                    prepare = connection.prepareStatement(updateEmData);
                    prepare.setString(1, Employee_password.getText());
                    prepare.setString(2, Employee_fullName.getText());
                    prepare.setString(3, Employee_gender.getSelectionModel().getSelectedItem());
                    prepare.setString(4, Employee_username.getText());

                    prepare.executeUpdate();

                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Success");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully Updated");
                    alert.showAndWait();

                    showEmployeeDataList();
                    reset();

                } else {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Cancelled");
                    alert.setHeaderText(null);
                    alert.setContentText("Update cancelled!");
                    alert.showAndWait();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("An error occurred while updating the employee.");
                alert.showAndWait();
            } finally {
                // Close resources if necessary
                if (prepare != null) {
                    try {
                        prepare.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (connection != null) {
                    try {
                        connection.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public void deleteEmployee() {
        // Get the selected employee from the table view
        EmployeeData selectedEmployee = employee_table.getSelectionModel().getSelectedItem();

        // Check if an employee is selected
        if (selectedEmployee == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please select an employee to delete.");
            alert.showAndWait();
            return;
        }

        // Get the username of the selected employee (or any other unique identifier)
        String username = selectedEmployee.getUsername(); // Assuming you have a method to get the username

        // Create a confirmation alert
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to delete the employee with username: " + username + "?");

        // Show the alert and wait for user response
        Optional<ButtonType> result = alert.showAndWait();

        // If user confirms, proceed with deletion
        if (result.isPresent() && result.get() == ButtonType.OK) {
            String deleteQuery = "DELETE FROM employee WHERE username = ?"; // Assuming username is the unique identifier

            try {
                // Establish the database connection
                connection = Database.connnectionDB();
                prepare = connection.prepareStatement(deleteQuery);
                prepare.setString(1, username); // Set the username in the prepared statement

                // Execute the delete operation
                int rowsAffected = prepare.executeUpdate();

                // Check if the deletion was successful
                if (rowsAffected > 0) {
                    Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                    successAlert.setTitle("Success");
                    successAlert.setHeaderText(null);
                    successAlert.setContentText("Employee deleted successfully.");
                    successAlert.showAndWait();

                    // Refresh the employee list
                    showEmployeeDataList(); // Assuming you have a method to refresh the employee data list
                } else {
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setTitle("Error");
                    errorAlert.setHeaderText(null);
                    errorAlert.setContentText("Failed to delete the employee. Please try again.");
                    errorAlert.showAndWait();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Error");
                errorAlert.setHeaderText(null);
                errorAlert.setContentText("An error occurred while deleting the employee.");
                errorAlert.showAndWait();
            } finally {
                // Close resources if applicable
                try {
                    if (prepare != null) prepare.close();
                    if (connection != null) connection.close();
                } catch (Exception e) {
                    e.printStackTrace(); // Handle closing exceptions
                }
            }
        }
    }

    //For Product
    public void searchProd() {
        FilteredList<ProductData> filter = new FilteredList<>(addProductsList, e -> true);
        product_search.textProperty().addListener((observable, oldValue, newValue) -> {
            filter.setPredicate(predicateProductData -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true; // Show all products if search is empty
                }
                String lowerCaseFilter = newValue.toLowerCase();
                return predicateProductData.getProductID().toLowerCase().contains(lowerCaseFilter) ||
                        predicateProductData.getBrand().toLowerCase().contains(lowerCaseFilter) ||
                        predicateProductData.getStatus().toLowerCase().contains(lowerCaseFilter) ||
                        predicateProductData.getProductName().toLowerCase().contains(lowerCaseFilter)||
                        String.valueOf(predicateProductData.getPrice()).contains(lowerCaseFilter);
            });
        });

        SortedList<ProductData> sortList = new SortedList<>(filter);
        sortList.comparatorProperty().bind(product_tableview.comparatorProperty());
        product_tableview.setItems(sortList);
    }

    public ObservableList<ProductData> addproductListData(){
        ObservableList<ProductData> prodList = FXCollections.observableArrayList();

        String sql = "SELECT * FROM product";
        connection = Database.connnectionDB();

        try{

            ProductData prod;
            prepare = connection.prepareStatement(sql);
            result = prepare.executeQuery();

            while (result.next()){
                prod = new ProductData(result.getString("product_id"),
                        result.getString("name"),
                        result.getString("brand_name"),
                        result.getDouble("price"),
                        result.getString("status"),
                        result.getString("image"));

                prodList.add(prod);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return prodList;
    }

    private ObservableList<ProductData> addProductsList;
    public void showDataList(){
        addProductsList = addproductListData();

        product_col_id.setCellValueFactory(new PropertyValueFactory<>("productID"));
        product_col_name.setCellValueFactory(new PropertyValueFactory<>("productName"));
        product_col_brand.setCellValueFactory(new PropertyValueFactory<>("brand"));
        product_col_price.setCellValueFactory(new PropertyValueFactory<>("price"));
        product_col_status.setCellValueFactory(new PropertyValueFactory<>("status"));

        product_tableview.setItems(addProductsList);
    }

    public void switchforms(javafx.event.ActionEvent event){
        if(event.getSource() == dashboard_btn){
            admin_dashboard_form.setVisible(true);
            product_form.setVisible(false);
            Employee_form.setVisible(false);
            loadReviews();
            showDataList();
        }
        if(event.getSource() == addProduct_btn){
            admin_dashboard_form.setVisible(false);
            product_form.setVisible(true);
            Employee_form.setVisible(false);
            showDataList();
            productListStatus();
            searchProd();
        }
        if(event.getSource() == employee_btn){
            admin_dashboard_form.setVisible(false);
            product_form.setVisible(false);
            Employee_form.setVisible(true);
            showEmployeeDataList();
            employeeGenderSelect();
        }
    }

    public void select(){
        ProductData prod = product_tableview.getSelectionModel().getSelectedItem();
        int n = product_tableview.getSelectionModel().getSelectedIndex();

        if((n - 1) < -1){
            return;
        }

        product_Id.setText(prod.getProductID());
        product_brand.setText(prod.getBrand());
        product_name.setText(prod.getProductName());
        product_price.setText(String.valueOf(prod.getPrice()));

        data.path = prod.getImage();
        String path = "File:"+prod.getImage();
        img = new Image(path, 194, 153, false, true);
        product_image.setImage(img);
    }


    private String[] statusList = {"Available", "Out of Stock"};
    public void productListStatus(){
        List<String>lists = new ArrayList<>();

        for(String data:statusList){
            lists.add(data);
        }

        ObservableList statusData = FXCollections.observableArrayList(lists);
        product_status.setItems(statusData);
    }


    public void image() {
        FileChooser productFile = new FileChooser();
        productFile.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image file", "*png", "*jpg"));

        File file = productFile.showOpenDialog(main_form.getScene().getWindow());

        if (file != null) {

            data.path = file.getAbsolutePath();
            img = new Image(file.toURI().toString(), 150, 160, false, true);
            product_image.setImage(img);
        }
    }


    public void add() {
        if (product_Id.getText().isEmpty() ||
                product_name.getText().isEmpty() ||
                product_brand.getText().isEmpty() ||
                product_price.getText().isEmpty() ||
                product_status.getSelectionModel().getSelectedItem() == null ||
                data.path == null) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please fill up the blank field");
            alert.showAndWait(); // Show the alert
        } else {
            String chckprod = "SELECT product_id FROM product WHERE product_id = ?";

            try {
                // Ensure the connection is initialized
                connection = Database.connnectionDB();
                prepare = connection.prepareStatement(chckprod);
                prepare.setString(1, product_Id.getText()); // Use prepared statement to prevent SQL injection
                result = prepare.executeQuery();

                if (result.next()) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText(product_Id.getText() + " already exists");
                    alert.showAndWait(); // Show the alert
                } else {
                    String insertdata = "INSERT INTO product (product_id, name, brand_name, price, status, image) VALUES (?, ?, ?, ?, ?, ?)";
                    prepare = connection.prepareStatement(insertdata);
                    prepare.setString(1, product_Id.getText());
                    prepare.setString(2, product_name.getText());
                    prepare.setString(3, product_brand.getText());
                    prepare.setString(4, product_price.getText());
                    prepare.setString(5, (String) product_status.getSelectionModel().getSelectedItem());
                    String path = data.path.replace("\\", "\\\\"); // Ensure the path is correctly formatted
                    prepare.setString(6, path);

                    prepare.executeUpdate();

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Success");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully Added!");
                    alert.showAndWait(); // Show the alert

                    showDataList();
                    clear();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("An error occurred while adding the product.");
                alert.showAndWait(); // Show the alert
            }
        }
    }

    public void clear(){
        product_Id.setText("");
        product_name.setText("");
        product_brand.setText("");
        product_price.setText("");
        product_status.getSelectionModel().clearSelection();
        data.path = "";
        product_image.setImage(null);
    }

    public void delete() {
        // Get the selected product from the table view
        ProductData selectedProduct = product_tableview.getSelectionModel().getSelectedItem();

        // Check if a product is selected
        if (selectedProduct == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please select a product to delete.");
            alert.showAndWait();
            return;
        }

        // Get the product ID of the selected product
        String productId = selectedProduct.getProductID();

        // Create a confirmation alert
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to delete the product with ID: " + productId + "?");

        // Show the alert and wait for user response
        Optional<ButtonType> result = alert.showAndWait();

        // If user confirms, proceed with deletion
        if (result.isPresent() && result.get() == ButtonType.OK) {
            String deleteQuery = "DELETE FROM product WHERE product_id = ?";

            try {
                // Establish the database connection
                connection = Database.connnectionDB();
                prepare = connection.prepareStatement(deleteQuery);
                prepare.setString(1, productId); // Set the product ID in the prepared statement

                // Execute the delete operation
                int rowsAffected = prepare.executeUpdate();

                // Check if the deletion was successful
                if (rowsAffected > 0) {
                    Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                    successAlert.setTitle("Success");
                    successAlert.setHeaderText(null);
                    successAlert.setContentText("Product deleted successfully.");
                    successAlert.showAndWait();

                    // Refresh the product list
                    showDataList();
                } else {
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setTitle("Error");
                    errorAlert.setHeaderText(null);
                    errorAlert.setContentText("Failed to delete the product. Please try again.");
                    errorAlert.showAndWait();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Error");
                errorAlert.setHeaderText(null);
                errorAlert.setContentText("An error occurred while deleting the product.");
                errorAlert.showAndWait();
            }
        }
    }

    public void update() {
        if (product_Id.getText().isEmpty() ||
                product_name.getText().isEmpty() ||
                product_brand.getText().isEmpty() ||
                product_price.getText().isEmpty() ||
                product_status.getSelectionModel().getSelectedItem() == null ||
                data.path == null) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please fill up the blank field");
            alert.showAndWait();
        } else {
            String path = data.path.replace("\\", "\\\\");
            String updateData = "UPDATE product SET " +
                    "name = '" + product_name.getText() + "', " +
                    "brand_name = '" + product_brand.getText() + "', " +
                    "price = '" + product_price.getText() + "', " +
                    "status = '" + product_status.getSelectionModel().getSelectedItem() + "', " +
                    "image = '" + path + "' " +
                    "WHERE product_id = '" + product_Id.getText() + "'";

            connection = Database.connnectionDB();

            try {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure you want to update " + product_Id.getText() + "?");
                Optional<ButtonType> option = alert.showAndWait();

                if (option.isPresent() && option.get() == ButtonType.OK) {
                    prepare = connection.prepareStatement(updateData);
                    prepare.executeUpdate();

                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Success");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully Updated");
                    alert.showAndWait();

                    showDataList();
                    clear();
                } else {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Cancelled");
                    alert.setHeaderText(null);
                    alert.setContentText("Update cancelled!");
                    alert.showAndWait();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("An error occurred while updating the product.");
                alert.showAndWait();
            }
        }
    }


    public void logout() {
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
                FXMLLoader loader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
                Parent root = loader.load();

                // Get current stage and set the new scene
                Stage stage = (Stage) logout_btn.getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.centerOnScreen();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

