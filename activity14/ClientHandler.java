package com.chatapp.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

public class ClientHandler implements Runnable {

    private static Set<ClientHandler> clients = new HashSet<>();

    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private String username;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Ask for username
            out.println("Enter your username:");
            username = in.readLine();

            synchronized (clients) {
                clients.add(this);
            }

            out.println("Welcome, " + username + "!");
            broadcast(username + " has joined the chat!");

            String message;
            while ((message = in.readLine()) != null) {

                if (message.equalsIgnoreCase("exit")) {
                    break;
                }

                broadcast(username + ": " + message);
            }

        } catch (IOException e) {
            System.out.println("Connection error: " + e.getMessage());
        } finally {
            cleanup();
        }
    }

    private void broadcast(String message) {
        synchronized (clients) {
            for (ClientHandler client : clients) {
                client.out.println(message);
            }
        }
    }

    private void cleanup() {
        try {
            if (username != null) {
                broadcast(username + " has left the chat.");
            }

            synchronized (clients) {
                clients.remove(this);
            }

            if (socket != null) socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}