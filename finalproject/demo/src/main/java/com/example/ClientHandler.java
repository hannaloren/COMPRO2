package com.example;

import java.io.*;
import java.net.*;

public class ClientHandler extends Thread {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private String playerName = "Anonymous";
    private int score = 0;

    public ClientHandler(Socket socket) {
        this.socket = socket;
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            // Handle Registration
            out.println("ENTER_NAME");
            String input = in.readLine();
            if (input != null && !input.trim().isEmpty()) {
                this.playerName = input.trim();
            }
            System.out.println("Player registered: " + this.playerName);
            out.println("Welcome " + this.playerName + "! Wait for admin to start...");

            // Keep thread alive but do nothing; the Server will call receiveAnswer()
            while (!socket.isClosed()) {
                Thread.sleep(1000);
            }
        } catch (Exception e) {
            System.out.println(playerName + " disconnected.");
        } finally {
            cleanup();
        }
    }

    public void sendMessage(String msg) {
        out.println(msg);
    }

    public String receiveAnswer() {
        try {
            // Wait for exactly one line of input for the current question
            return in.readLine();
        } catch (IOException e) {
            return null;
        }
    }

    private void cleanup() {
        try {
            if (socket != null) socket.close();
        } catch (IOException e) { e.printStackTrace(); }
    }

    public String getPlayerName() { return playerName; }
    public int getScore() { return score; }
    public void addScore() { score++; }
}