package com.chatapp;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.chatapp.model.ClientHandler;

public class Main {
    public static void main(String[] args) {
        System.out.println("Welcome to the Chat App!");
        int port = 8000;

        System.out.println("Chat server started");

        try (ServerSocket serverSocket = new ServerSocket(port)) {

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected (IP: " + clientSocket.getInetAddress() + ")");

                ClientHandler handler = new ClientHandler(clientSocket);
                Thread thread = new Thread(handler);
                thread.start();
            }

        } catch (IOException e) {
            System.out.println("Server error: " + e.getMessage());
        }

    }
}