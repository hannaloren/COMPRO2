package com.example;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class QuizClient {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 12345);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                Scanner scanner = new Scanner(System.in)) {

            String serverMsg;
            while ((serverMsg = in.readLine()) != null) {
                if (serverMsg.equals("ENTER_NAME")) {
                    System.out.print("Enter your username: ");
                    out.println(scanner.nextLine());
                } else if (serverMsg.equals("WAITING_FOR_ANSWER")) {
                    System.out.print("Your Answer > ");
                    if (scanner.hasNextLine()) {
                        out.println(scanner.nextLine());
                    }
                } else if (serverMsg.equals("GAME_OVER")) {
                    System.out.println("Game finished. Disconnecting...");
                    break;
                } else {
                    // Regular message (Questions, Score, etc.)
                    System.out.println(serverMsg);
                }
            }
        } catch (IOException e) {
            System.err.println("Disconnected from server.");
        }
    }
}