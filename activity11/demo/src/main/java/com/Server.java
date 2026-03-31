package com;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.logging.FileHandler;

import com.rpc.Move;
import com.rpc.Player;

public class Server {

    public static void main(String[] args) {
        int port = 8000;

        ArrayList<Player> users = new ArrayList<>();

        try (ServerSocket server = new ServerSocket(port)) {

            System.out.println("Waiting for players...");

            Socket c1 = server.accept();
            Socket c2 = server.accept();

            BufferedReader in1 = new BufferedReader(new InputStreamReader(c1.getInputStream()));
            PrintWriter out1 = new PrintWriter(c1.getOutputStream(), true);

            BufferedReader in2 = new BufferedReader(new InputStreamReader(c2.getInputStream()));
            PrintWriter out2 = new PrintWriter(c2.getOutputStream(), true);

            Player p1 = login(out1, in1, users);
            Player p2 = login(out2, in2, users);

            out1.println("Enter move:");
            p1.setMove(Move.fromString(in1.readLine()));

            out2.println("Enter move:");
            p2.setMove(Move.fromString(in2.readLine()));

        }
    }

    private static Player login(PrintWriter out, BufferedReader in, ArrayList<Player> users) throws IOException {
        while (true) {
            out.println("Username:");
            String u = in.readLine();

            out.println("Password:");
            String p = in.readLine();

            Player player = UserDataManager.login(users, u, p);

            if (player != null) {
                out.println("Login successful!");
                return player;
            } else {
                out.println("Invalid. Try again.");
            }
        }
    }
}