package com.example;

import com.google.gson.*;
import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class QuizClient {
    private static final String SERVER = "localhost";
    private static final int PORT = 8080;
    private static volatile boolean canSend = false;

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER, PORT);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                Scanner sc = new Scanner(System.in)) {

            // Separate thread for Heartbeat
            new Thread(() -> {
                while (!socket.isClosed()) {
                    out.println("PING");
                    try {
                        Thread.sleep(10000);
                    } catch (Exception ignored) {
                    }
                }
            }).start();

            String msg;
            while ((msg = in.readLine()) != null) {
                if (msg.equals("ENTER_NAME")) {
                    System.out.print("Name: ");
                    out.println(sc.nextLine());
                } else if (msg.equals("ENTER_PASSWORD")) {
                    System.out.print("Password: ");
                    out.println(sc.nextLine());
                } else if (msg.equals("ENTER_CODE")) {
                    System.out.print("Code: ");
                    out.println(sc.nextLine());
                } else if (msg.equals("VERIFIED")) {
                    System.out.println("Logged in! Wait for Start...");
                } else if (msg.startsWith("TIMER:")) {
                    System.out.print("\r" + msg + "   ");
                } else if (msg.equals("ANSWER_NOW")) {
                    System.out.print("\nYour Answer: ");
                    canSend = true;
                    // We block here to get the answer
                    if (sc.hasNextLine())
                        out.println(sc.nextLine());
                    canSend = false;
                } else if (msg.startsWith("{")) {
                    JsonObject obj = JsonParser.parseString(msg).getAsJsonObject();
                    System.out.println("\n\n" + obj.get("question").getAsString());
                    if (obj.has("choices")) {
                        JsonObject c = obj.getAsJsonObject("choices");
                        c.entrySet().forEach(e -> System.out.println(e.getKey() + ": " + e.getValue().getAsString()));
                    }
                } else {
                    System.out.println("\n" + msg);
                }
            }
        } catch (Exception e) {
            System.err.println("Disconnected: " + e.getMessage());
        }
    }
}