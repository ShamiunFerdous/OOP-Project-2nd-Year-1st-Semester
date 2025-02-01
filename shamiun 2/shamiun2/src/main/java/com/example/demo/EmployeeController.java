package com.example.demo;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.print.PrinterJob;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.*;
import java.util.Optional;
import java.util.ResourceBundle;

public class EmployeeController implements Initializable {

    @FXML
    private Button back_button;

    @FXML
    private TableColumn<PurchaseItem,String> brandColumn;

    @FXML
    private Label customerNameLabel;

    @FXML
    private AnchorPane customer_dashboard;


    @FXML
    private Button discardbtn;

    @FXML
    private Label employeeName;

    @FXML
    private AnchorPane left_form;

    @FXML
    private Button logOut;

    @FXML
    private TableColumn<PurchaseItem, Double> priceColumn;

    @FXML
    private TableColumn<PurchaseItem, String> productColumn;

    @FXML
    private Button purchase_addbtn;

    @FXML
    private TextField purchase_brand;

    @FXML
    private Button purchase_clearallbtn;

    @FXML
    private TableColumn<PurchaseItem, String> purchase_col_brandname;

    @FXML
    private TableColumn<PurchaseItem, Double> purchase_col_price;

    @FXML
    private TableColumn<PurchaseItem, String> purchase_col_productname_name;

    @FXML
    private TableColumn<PurchaseItem, Integer> purchase_col_quantity;

    @FXML
    private Button purchase_paybtn;

    @FXML
    private Spinner<Integer> purchase_quantity;


    @FXML
    private Button print_receipt;


    @FXML
    private Button purchase_searchbtn;

    @FXML
    private TableView<PurchaseItem> purchase_tableview;

    @FXML
    private Label purchase_totalprice;

    @FXML
    private TableColumn<ProductData, Integer> quantityColumn;

    @FXML
    private TableView<PurchaseItem> receiptTable;

    @FXML
    private Label receiptTitle;

    @FXML
    private ListView<ProductData> product_list;

    @FXML
    private Label receipt_customername;

    @FXML
    private AnchorPane receipt_form;

    @FXML
    private Button receipt_button;

    @FXML
    private Button messenger_server;


    @FXML
    private AnchorPane right_form;

    @FXML
    private Label total;

    @FXML
    private Label totalPriceLabel;

    @FXML
    private Label totalPriceValueLabel;

    @FXML
    private BorderPane up_level;

    @FXML
    private ImageView user_img;

    @FXML
    private Label welcome;

    private ObservableList<PurchaseItem> purchaseList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        employeeName.setText("Welcome!");

        // Initialize the purchase list
        purchaseList = FXCollections.observableArrayList();

        // Set up the TableView columns for purchase table
        purchase_col_productname_name.setCellValueFactory(new PropertyValueFactory<>("productName"));
        purchase_col_brandname.setCellValueFactory(new PropertyValueFactory<>("brand"));
        purchase_col_quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        purchase_col_price.setCellValueFactory(new PropertyValueFactory<>("price"));

        // Set the items in the TableView
        purchase_tableview.setItems(purchaseList);

        // Initialize the spinner
        purchase_quantity.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 1));

        // Load product data
        loadProductData();
        receipt_form.setVisible(false);
        receipt_form.setManaged(false);

        // Set up the receipt table columns
        brandColumn.setCellValueFactory(cellData -> cellData.getValue().brandProperty());
        productColumn.setCellValueFactory(cellData -> cellData.getValue().productNameProperty());
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        priceColumn.setCellValueFactory(cellData -> cellData.getValue().priceProperty().asObject());
    }

    private void populateReceiptTable() {
        // Create an ObservableList for the receipt items
        ObservableList<PurchaseItem> receiptItems = FXCollections.observableArrayList(purchaseList);

        // Set the items in the receipt table
        receiptTable.setItems(receiptItems);

        // Set the total price in the totalPriceValueLabel
        double totalPrice = 0.0;
        for (PurchaseItem item : receiptItems) {
            totalPrice += item.getPrice();
        }
        totalPriceValueLabel.setText(String.format("$%.2f", totalPrice));

        // Set up the receipt table columns (if not already set)
        brandColumn.setCellValueFactory(cellData -> cellData.getValue().brandProperty());
        productColumn.setCellValueFactory(cellData -> cellData.getValue().productNameProperty());
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        priceColumn.setCellValueFactory(cellData -> cellData.getValue().priceProperty().asObject());
    }

    private String username;
    public void displayname(String username) {
        this.username = username;
        employeeName.setText(username);
        loadProductData();
    }

    private ObservableList<ProductData> productList;
    public void loadProductData() {
        productList = FXCollections.observableArrayList();
        String sql = "SELECT * FROM product";
        try (Connection connection = Database.connnectionDB();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                ProductData product = new ProductData(
                        resultSet.getString("product_id"),
                        resultSet.getString("name"),
                        resultSet.getString("brand_name"),
                        resultSet.getDouble("price"),
                        resultSet.getString("status"),
                        resultSet.getString("image")
                );
                productList.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Set the items in the ListView
        product_list.setItems(productList);

        // Optionally, customize how the items are displayed
        product_list.setCellFactory(param -> new ListCell<ProductData>() {
            @Override
            protected void updateItem(ProductData item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getProductName() + " - " + item.getBrand() + " - $" + item.getPrice());
                }
            }
        });
    }

    @FXML
    private void handleSearchButtonAction() {
        String brandName = purchase_brand.getText().trim(); // Get the brand name from the text field
        if (brandName.isEmpty()) {
            loadProductData(); // If the text field is empty, load all products
            return;
        }

        ObservableList<ProductData> filteredList = FXCollections.observableArrayList();
        String sql = "SELECT * FROM product WHERE brand_name = ?";
        try (Connection connection = Database.connnectionDB();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, brandName);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                ProductData product = new ProductData(
                        resultSet.getString("product_id"),
                        resultSet.getString("name"),
                        resultSet.getString("brand_name"),
                        resultSet.getDouble("price"),
                        resultSet.getString("status"),
                        resultSet.getString("image")
                );
                filteredList.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Set the filtered list to the ListView
        product_list.setItems(filteredList);
    }

    @FXML
    private void handleAddButtonAction() {
        // Get the selected product from the ListView
        ProductData selectedProduct = product_list.getSelectionModel().getSelectedItem();

        // Check if a product is selected
        if (selectedProduct != null) {
            // Get the quantity from the Spinner
            int quantity = purchase_quantity.getValue();

            // Create a new PurchaseItem
            PurchaseItem purchaseItem = new PurchaseItem(
                    selectedProduct.getProductName(),
                    selectedProduct.getBrand(),
                    quantity,
                    selectedProduct.getPrice() * quantity // Total price for this item
            );

            // Add the PurchaseItem to the purchase list
            purchaseList.add(purchaseItem);

            // Update the total price label
            updateTotalPrice();

            // Clear the selection and reset the spinner
            product_list.getSelectionModel().clearSelection();
            purchase_quantity.getValueFactory().setValue(1); // Reset to default quantity
        } else {
            // Show an alert if no product is selected
            showAlert("No Product Selected", "Please select a product to add to the purchase.");
        }
    }

    private void updateTotalPrice() {
        double totalPrice = 0.0;

        // Calculate the total price from the purchaseList
        for (PurchaseItem item : purchaseList) {
            totalPrice += item.getPrice(); // Assuming price is the total price for that item
        }

        // Update the label to show the total price
        purchase_totalprice.setText(String.format("$%.2f", totalPrice));
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void handleClearAllButtonAction() {
        // Clear all items from the purchase list
        purchaseList.clear();

        // Update the total price label to reflect the change
        updateTotalPrice(); // This will set the total price to $0.00
    }

    @FXML
    private void PrintReceipt() {
        Stage stage = (Stage) print_receipt.getScene().getWindow();
        PrinterJob job = PrinterJob.createPrinterJob();
        if (job != null) {
            if (job.showPrintDialog(stage)) { // Shows system print dialog
                boolean success = job.printPage(stage.getScene().getRoot());
                if (success) {
                    job.endJob();
                    System.out.println("PDF saved successfully!");
                } else {
                    System.out.println("Failed to save PDF.");
                }
            }
        }
    }

    public void ServerMessenger() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("server-view.fxml"));
            Parent root = loader.load();

            ServerController serverController = loader.getController();
            serverController.setUsername(this.username);

            Stage stage = (Stage) messenger_server.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.centerOnScreen();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void handlePayButtonAction() {
        // Check if there are items in the purchase list
        if (purchaseList.isEmpty()) {
            showAlert("No Items", "There are no items to pay for.");
            return;
        }

        // Calculate the total price
        double totalPrice = 0.0;
        for (PurchaseItem item : purchaseList) {
            totalPrice += item.getPrice();
        }

        // Simulate payment processing
        boolean paymentSuccessful = processPayment(totalPrice); // Simulate payment processing

        if (paymentSuccessful) {
            // Populate the receipt table with the purchase items
            populateReceiptTable();

            // Show the receipt form
            handleReceiptButtonAction(null); // Call the method to show the receipt form
            updateTotalPrice(); // This will set the total price to $0.00
        } else {
            showAlert("Payment Failed", "There was an issue processing your payment. Please try again.");
        }
    }

    // Simulated payment processing method
    private boolean processPayment(double amount) {
        // In a real application, you would integrate with a payment gateway here
        // For this example, we will just return true to simulate a successful payment
        return true;
    }


    @FXML
    public void handleReceiptButtonAction(ActionEvent event) {
        // Hide the customer dashboard
        customer_dashboard.setVisible(false);
        customer_dashboard.setManaged(false); // Optional: to prevent layout issues

        populateReceiptTable();
        purchaseList.clear();

        // Show the receipt form
        receipt_form.setVisible(true);
        receipt_form.setManaged(true); // Optional: to ensure it is part of the layout
    }

    @FXML
    public void handleDiscardButtonAction(ActionEvent event) {
        product_list.getSelectionModel().clearSelection(); // Clear the selection
        purchase_quantity.getValueFactory().setValue(1);
    }

    @FXML
    private void handleBackButtonAction(ActionEvent event) {
        // Hide the receipt form
        receipt_form.setVisible(false);
        receipt_form.setManaged(false); // Optional: to prevent layout issues

        // Show the customer dashboard
        customer_dashboard.setVisible(true);
        customer_dashboard.setManaged(true); // Optional: to ensure it is part of the layout
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
                Stage stage = (Stage) logOut.getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.centerOnScreen();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}