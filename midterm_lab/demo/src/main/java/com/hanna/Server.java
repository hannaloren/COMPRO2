package com.hanna;

import java.net.ServerSocket;
import java.net.Socket;

import com.hanna.model.GameSession;

public class Server {

    private static Socket player1;
    private static Socket player2;

    public static void main(String[] args) {
        int port = 8000;

        try (ServerSocket server = new ServerSocket(port)) {
            System.out.println("Waiting for players...");

            player1 = server.accept();
            System.out.println("Player 1 connected");

            player2 = server.accept();
            System.out.println("Player 2 connected");

            new GameSession(player1, player2).start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
