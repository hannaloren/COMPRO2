package com.chatapp.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
    public static void main(String[] args) {
        String host = "localhost";
        int port = 8000;

        try (
                Socket socket = new Socket(host, port);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in))) {

            System.out.println("Connected to chat server!");

            Thread listener = new Thread(() -> {
                try {
                    String response;
                    while ((response = in.readLine()) != null) {
                        System.out.println("\n" + response);

                    }
                } catch (IOException e) {
                    System.out.println("Disconnected from server.");
                }
            });

            listener.start();

            // Send messages
            String message;
            while ((message = userInput.readLine()) != null) {
                out.println(message);

                if (message.equalsIgnoreCase("exit")) {
                    break;
                }
            }
            System.out.println("Exiting chat...");

        } catch (IOException e) {
            System.out.println("Client error: " + e.getMessage());
        }
    }
}