package com.example;

import com.google.gson.*;
import java.io.*;
import java.net.*;

public class ClientHandler extends Thread {

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    private String playerName;
    private int score;dddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddm

    public ClientHandler(Socket socket) {
        this.socket = socket;

        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            out.println("ENTER_NAME");
            playerName = in.readLine();

            System.out.println(playerName + " joined the game.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                Thread.sleep(1000);
            }
        } catch (Exception e) {
            System.out.println(playerName + " disconnected.");
        }
    }

    public void sendMessage(String msg) {
        out.println(msg);
    }

    public String receiveAnswer() {
        try {
            socket.setSoTimeout(10000);
            return in.readLine();
        } catch (Exception e) {
            return null;
        }
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getScore() {
        return score;
    }

    public void addScore() {
        score++;
    }
}
