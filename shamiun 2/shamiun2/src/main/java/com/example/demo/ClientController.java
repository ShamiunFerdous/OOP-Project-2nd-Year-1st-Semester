package com.example.demo;

import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class ClientController implements Initializable {

    @FXML
    private Button back_customerBtn;

    @FXML
    public AnchorPane ap_main;
    @FXML
    private VBox vbox_messages;

    @FXML
    private TextField tf_message;

    @FXML
    private Button button_send;

    @FXML
    private Button clientlogout;


    @FXML
    private ScrollPane sp_main;

    private com.example.demo.Client client;

    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        //ap_main.getStylesheets().add(getClass().getResource("messenger_styles.css").toExternalForm());
        try {
            client = new com.example.demo.Client(new Socket("localhost", 1234));
            System.out.println("Connected to Server.");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Automatically scroll to the bottom when new messages are added
        vbox_messages.heightProperty().addListener((ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) ->
                sp_main.setVvalue((Double) newValue)
        );

        // Safely receive messages from the server
        new Thread(() -> {
            if (client != null) {
                client.receiveMessageFromServer(vbox_messages);
            }
        }).start();

        // Set up the "Send" button action
//        button_send.setOnAction((ActionEvent actionEvent) -> {
//            String messageToSend = tf_message.getText();
//            if (!messageToSend.isEmpty()) {
//                HBox hbox = new HBox();
//                hbox.setAlignment(Pos.CENTER_RIGHT);
//                hbox.setPadding(new Insets(5, 5, 5, 10));
//
//                Text text = new Text(messageToSend);
//                TextFlow flow = new TextFlow(text);
//                flow.setStyle("-fx-color: rgb(239,242,255);" +
//                        "-fx-background-color: rgb(15,125,242);" +
//                        "-fx-background-radius: 20px;");
//                flow.setPadding(new Insets(5, 10, 5, 10));
//                text.setFill(Color.color(0.934, 0.945, 0.996));
//                hbox.getChildren().add(flow);
//                vbox_messages.getChildren().add(hbox);
//                client.sendMessageToServer(messageToSend);
//                tf_message.clear();
//            }
//        });
        button_send.setOnAction((ActionEvent actionEvent) -> {
            String messageToSend = tf_message.getText();
            if (!messageToSend.isEmpty()) {
                HBox hbox = new HBox();
                hbox.setAlignment(Pos.CENTER_RIGHT);
                hbox.setPadding(new Insets(5, 5, 5, 10));

                VBox vbox = new VBox();
                vbox.setAlignment(Pos.CENTER_RIGHT);

                Text senderText = new Text("User");
                senderText.setStyle("-fx-font-weight: bold;");

                Text text = new Text(messageToSend);
                text.setStyle("-fx-font-size: 14px;");
                TextFlow flow = new TextFlow(text);
                flow.setStyle("-fx-color: rgb(239,242,255);" +
                        "-fx-background-color: rgb(15,125,242);" +
                        "-fx-background-radius: 20px;");
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
                client.sendMessageToServer(messageToSend);
                tf_message.clear();
            }
        });

//        button_send.setOnAction((ActionEvent actionEvent) -> {
//            String messageToSend = tf_message.getText();
//            if (!messageToSend.isEmpty()) {
//                HBox hbox = new HBox();
//                hbox.setAlignment(Pos.CENTER_RIGHT);
//                hbox.setPadding(new Insets(5, 5, 5, 10));
//
//                VBox vbox = new VBox();
//                vbox.setAlignment(Pos.CENTER_RIGHT);
//
//                Text senderText = new Text("User");
//                senderText.setStyle("-fx-font-weight: bold;");
//
//                Text text = new Text(messageToSend);
//                TextFlow flow = new TextFlow(text);
//                flow.setStyle("-fx-color: rgb(239,242,255);" +
//                        "-fx-background-color: rgb(15,125,242);" +
//                        "-fx-background-radius: 20px;");
//                flow.setPadding(new Insets(5, 10, 5, 10));
//                text.setFill(Color.color(0.934, 0.945, 0.996));
//
//                vbox.getChildren().addAll(senderText, flow);
//                hbox.getChildren().add(vbox);
//
//                vbox_messages.getChildren().add(hbox);
//                client.sendMessageToServer(messageToSend);
//                tf_message.clear();
//            }
//        });

    }

    //    public static void addLabel(String messageFromServer, VBox vbox) {
//        HBox hBox = new HBox();
//        hBox.setAlignment(Pos.CENTER_LEFT);
//        hBox.setPadding(new Insets(5, 5, 5, 10));
//
//        VBox vboxWrapper = new VBox();
//        vboxWrapper.setAlignment(Pos.CENTER_LEFT);
//
//        Text senderText = new Text("Employee");
//        senderText.setStyle("-fx-font-weight: bold;");
//
//        Text text = new Text(messageFromServer);
//        TextFlow textFlow = new TextFlow(text);
//        textFlow.setStyle("-fx-background-color: rgb(233,233,235);" +
//                "-fx-background-radius: 20px;");
//        textFlow.setPadding(new Insets(5, 5, 5, 10));
//
//        vboxWrapper.getChildren().addAll(senderText, textFlow);
//        hBox.getChildren().add(vboxWrapper);
//
//        // Ensure UI updates happen on the JavaFX Application Thread
//        Platform.runLater(() -> vbox.getChildren().add(hBox));
//    }
    public static void addLabel(String messageFromServer, VBox vbox) {
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.setPadding(new Insets(5, 5, 5, 10));

        VBox vboxWrapper = new VBox();
        vboxWrapper.setAlignment(Pos.CENTER_LEFT);

        Text senderText = new Text("Employee");
        senderText.setStyle("-fx-font-weight: bold;");

        // Format the message text
        Text text = new Text(messageFromServer);
        text.setStyle("-fx-font-size: 14px;");
        TextFlow textFlow = new TextFlow(text);
        textFlow.setStyle("-fx-background-color: rgb(233,233,235);" +
                "-fx-background-radius: 20px; -fx-padding: 5 10 5 10;");

        // Add timestamp formatted as hh:mm a
        String timestamp = java.time.LocalTime.now().format(java.time.format.DateTimeFormatter.ofPattern("hh:mm a"));
        Text timeText = new Text(timestamp);
        timeText.setStyle("-fx-font-size: 9px; -fx-text-fill: gray;");

        // Add timestamp in a new line within the same colored box
        VBox messageBox = new VBox(textFlow, timeText);
        messageBox.setAlignment(Pos.BOTTOM_RIGHT);

        vboxWrapper.getChildren().addAll(senderText, messageBox);
        hBox.getChildren().add(vboxWrapper);

        // Ensure UI updates happen on the JavaFX Application Thread
        Platform.runLater(() -> vbox.getChildren().add(hBox));
    }

    private CustdashController custdashController;

    public void setCustdashController(CustdashController custdashController) {
        this.custdashController = custdashController;
    }

    private String username;

    public void setUsername(String username) {
        this.username = username;
    }

    public void backTocustomer() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("customerdashboard-view.fxml"));
            Parent root = loader.load();
            CustdashController custdashController = loader.getController();

            // Set the username back to the CustdashController
            custdashController.setUsername(this.username); // Pass the username back

            custdashController.loadReviews(); // Call loadReviews here
            custdashController.display();

            Stage stage = (Stage) back_customerBtn.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.centerOnScreen();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void ClientLogOut(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) clientlogout.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.centerOnScreen();
        } catch (Exception e) {
            e.printStackTrace();
        }
//        Parent root = FXMLLoader.load(getClass().getResource("hello-view.fxml"));
//        Scene scene = new Scene(root);
//        stage.setTitle("Hello!");
//        stage.setScene(scene);
//        stage.show();
    }



//public static void addLabel(String messageFromServer, VBox vbox) {
//    HBox hBox = new HBox();
//    hBox.setAlignment(Pos.CENTER_LEFT);
//    hBox.setPadding(new Insets(5, 5, 5, 10));
//
//    // Load the profile picture
//    //ImageView profileImage = new ImageView(new Image("file:man.png"));
//    ImageView profileImage = new ImageView(new Image("468816520_989637346525308_7776148883240493391_n.jpg"));
//
//    profileImage.setFitWidth(40); // Set the width of the image
//    profileImage.setFitHeight(40); // Set the height of the image
//    profileImage.setStyle("-fx-background-radius: 20; -fx-border-radius: 20;");
//    profileImage.setPreserveRatio(true);
//
//    VBox vboxWrapper = new VBox();
//    vboxWrapper.setAlignment(Pos.CENTER_LEFT);
//
//    Text senderText = new Text("Employee");
//    senderText.setStyle("-fx-font-weight: bold;");
//
//    Text text = new Text(messageFromServer);
//    TextFlow textFlow = new TextFlow(text);
//    textFlow.setStyle("-fx-background-color: rgb(233,233,235);" +
//            "-fx-background-radius: 20px;");
//    textFlow.setPadding(new Insets(5, 5, 5, 10));
//
//    vboxWrapper.getChildren().addAll(senderText, textFlow);
//
//    // Add the profile image and the message box to the HBox
//    hBox.getChildren().addAll(profileImage, vboxWrapper);
//
//    // Ensure UI updates happen on the JavaFX Application Thread
//    Platform.runLater(() -> vbox.getChildren().add(hBox));
//}


}
