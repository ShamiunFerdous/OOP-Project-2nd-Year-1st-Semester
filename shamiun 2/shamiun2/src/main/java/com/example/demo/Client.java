package com.example.demo;

import javafx.scene.layout.VBox;

import java.io.*;
import java.net.Socket;

public class Client {
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;

    public Client(Socket socket) {
        try {
            this.socket = socket;
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException e) {
            System.out.println("Error initializing client.");
            e.printStackTrace();
            closeEverything();
        }
    }

    public void sendMessageToServer(String messageToServer) {
        try {
            bufferedWriter.write(messageToServer);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch (IOException e) {
            System.out.println("Error sending message to server.");
            e.printStackTrace();
            closeEverything();
        }
    }

    public void receiveMessageFromServer(VBox vbox) {
        new Thread(() -> {
            while (socket != null && socket.isConnected()) {
                try {
                    String messageFromServer = bufferedReader.readLine();
                    if (messageFromServer != null) {
                        ClientController.addLabel(messageFromServer, vbox);
                    } else {
                        System.out.println("Server has closed the connection.");
                        closeEverything();
                        break;
                    }
                } catch (IOException e) {
                    System.out.println("Error receiving message from server.");
                    e.printStackTrace();
                    closeEverything();
                    break;
                }
            }
        }).start();
    }

    private void closeEverything() {
        try {
            if (bufferedReader != null) bufferedReader.close();
            if (bufferedWriter != null) bufferedWriter.close();
            if (socket != null) socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
