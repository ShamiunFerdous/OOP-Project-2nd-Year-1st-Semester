package com.example.demo;

import javafx.application.Platform;
import javafx.scene.layout.VBox;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private ServerSocket serverSocket;
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private ServerController controller;

    public Server(ServerSocket serverSocket, ServerController controller) {
        this.serverSocket = serverSocket;
        this.controller = controller;
        new Thread(() -> {
            try {
                this.socket = serverSocket.accept();
                this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                receiveMessageFromClient();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void receiveMessageFromClient() {
        new Thread(() -> {
            while (socket != null && socket.isConnected()) {
                try {
                    String messageFromClient = bufferedReader.readLine();
                    if (messageFromClient != null) {
                        controller.addMessageFromClient(messageFromClient);
                    } else {
                        System.out.println("Client disconnected.");
                        break;
                    }
                } catch (IOException e) {
                    System.out.println("Error receiving message from the client");
                    e.printStackTrace();
                    break;
                }
            }
        }).start();
    }

    public void sendMessageToClient(String messageToClient) {
        try {
            if (socket != null && socket.isConnected()) {
                bufferedWriter.write(messageToClient);
                bufferedWriter.newLine();
                bufferedWriter.flush();
            } else {
                System.out.println("No client connected to send the message.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
