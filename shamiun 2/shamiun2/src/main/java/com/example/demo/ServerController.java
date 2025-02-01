package com.example.demo;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.URL;
import java.util.ResourceBundle;

public class ServerController implements Initializable {

    @FXML
    private Button employee_page_btn;


    @FXML
    public AnchorPane ap_main;
    @FXML
    private VBox vbox_messages;

    @FXML
    private TextField tf_message;

    @FXML
    private Button button_send;

    @FXML
    private ScrollPane sp_main;

    private com.example.demo.Server server;



    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        //ap_main.getStylesheets().add(getClass().getResource("messenger_styles.css").toExternalForm());
        try {
            server = new com.example.demo.Server(new ServerSocket(1234), this);
        } catch (IOException e) {
            e.printStackTrace();
        }

        vbox_messages.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) {
                sp_main.setVvalue((Double) newValue);
            }
        });

//        button_send.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent actionEvent) {
//                String messageToSend = tf_message.getText();
//                if (!messageToSend.isEmpty()) {
//                    HBox hbox = new HBox();
//                    hbox.setAlignment(Pos.CENTER_RIGHT);
//                    hbox.setPadding(new Insets(5, 5, 5, 10));
//                    VBox vbox = new VBox();
//                    vbox.setAlignment(Pos.CENTER_RIGHT);
//                    Text senderText = new Text("Employee");
//                    senderText.setStyle("-fx-font-weight: bold;");
//                    Text text = new Text(messageToSend);
//                    TextFlow flow = new TextFlow(text);
//                    flow.setStyle("-fx-color: rgb(239,242,255);" +
//                            "-fx-background-color: rgb(9,92,177);" +
//                            "-fx-background-radius: 5px;");
//                    flow.setPadding(new Insets(5, 10, 5, 10));
//                    text.setFill(Color.color(0.934, 0.945, 0.996));
//                    vbox.getChildren().addAll(senderText, flow);
//                    hbox.getChildren().add(vbox);
//                    vbox_messages.getChildren().add(hbox);
//
//                    server.sendMessageToClient(messageToSend);
//                    tf_message.clear();
//                }
//            }
//        });



        button_send.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String messageToSend = tf_message.getText();
                if (!messageToSend.isEmpty()) {
                    HBox hbox = new HBox();
                    hbox.setAlignment(Pos.CENTER_RIGHT);
                    hbox.setPadding(new Insets(5, 5, 5, 10));

                    VBox vbox = new VBox();
                    vbox.setAlignment(Pos.CENTER_RIGHT);

                    Text senderText = new Text("Employee");
                    senderText.setStyle("-fx-font-weight: bold;");

                    Text text = new Text(messageToSend);
                    text.setStyle("-fx-font-size: 14px;");
                    TextFlow flow = new TextFlow(text);
                    flow.setStyle("-fx-color: rgb(239,242,255);" +
                            "-fx-background-color: rgb(9,92,177);" +
                            "-fx-background-radius: 5px;");
                    flow.setPadding(new Insets(5, 10, 5, 10));
                    text.setFill(Color.color(0.934, 0.945, 0.996));

                    // Add timestamp formatted as hh:mm a
                    String timestamp = java.time.LocalTime.now().format(java.time.format.DateTimeFormatter.ofPattern("hh:mm a"));
                    Text timeText = new Text(timestamp);
                    timeText.setStyle("-fx-font-size: 9px; -fx-text-fill: gray;");

                    // Add timestamp in a new line within the same colored box
                    VBox messageBox = new VBox(flow, timeText);
                    messageBox.setAlignment(Pos.BOTTOM_RIGHT);

                    vbox.getChildren().addAll(senderText, messageBox);
                    hbox.getChildren().add(vbox);
                    vbox_messages.getChildren().add(hbox);

                    server.sendMessageToClient(messageToSend);
                    tf_message.clear();
                }
            }
        });
    }


    public void addMessageFromClient(String messageFromClient) {
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.setPadding(new Insets(5, 5, 5, 10));

        VBox vbox = new VBox();
        vbox.setAlignment(Pos.CENTER_LEFT);

        Text senderText = new Text("User");
        senderText.setStyle("-fx-font-weight: bold;");

        Text text = new Text(messageFromClient);
        text.setStyle("-fx-font-size: 14px;");
        TextFlow textFlow = new TextFlow(text);
        textFlow.setStyle("-fx-background-color: rgb(233,233,235);" +
                "-fx-background-radius: 5px;");
        textFlow.setPadding(new Insets(5, 5, 5, 10));

        // Add timestamp formatted as hh:mm a
        String timestamp = java.time.LocalTime.now().format(java.time.format.DateTimeFormatter.ofPattern("hh:mm a"));
        Text timeText = new Text(timestamp);
        timeText.setStyle("-fx-font-size: 9px; -fx-text-fill: gray;");

        // Add timestamp in a new line within the same colored box
        VBox messageBox = new VBox(textFlow, timeText);
        messageBox.setAlignment(Pos.BOTTOM_RIGHT);

        vbox.getChildren().addAll(senderText, messageBox);
        hBox.getChildren().add(vbox);

        Platform.runLater(() -> vbox_messages.getChildren().add(hBox)); // Update UI on the JavaFX thread
    }

    private String username;
    public void setUsername(String username) {
        this.username = username;
    }

    public void backToemployee(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("employeedashboard-view.fxml"));
            Parent root = loader.load();

            EmployeeController employeeController = loader.getController();
            employeeController.displayname(this.username);

            employeeController.loadProductData();

            Stage stage = (Stage) employee_page_btn.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.centerOnScreen();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


//    public void addMessageFromClient(String messageFromClient) {
//        HBox hBox = new HBox();
//        hBox.setAlignment(Pos.CENTER_LEFT);
//        hBox.setPadding(new Insets(5, 5, 5, 10));
//
//        VBox vbox = new VBox();
//        vbox.setAlignment(Pos.CENTER_LEFT);
//
//        Text senderText = new Text("User");
//        senderText.setStyle("-fx-font-weight: bold;");
//
//        Text text = new Text(messageFromClient);
//        TextFlow textFlow = new TextFlow(text);
//        textFlow.setStyle("-fx-background-color: rgb(233,233,235);" +
//                "-fx-background-radius: 5px;");
//        textFlow.setPadding(new Insets(5, 5, 5, 10));
//
//        vbox.getChildren().addAll(senderText, textFlow);
//        hBox.getChildren().add(vbox);
//
//        Platform.runLater(() -> vbox_messages.getChildren().add(hBox)); // Update UI on the JavaFX thread
//    }
}
