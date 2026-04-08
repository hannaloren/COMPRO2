package com.hanna;

import com.hanna.model.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        String SERVER = "localhost";
        int PORT = 8000;

        try (Socket socket = new Socket(SERVER, PORT);
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                Scanner sc = new Scanner(System.in)) {

            System.out.println("Connected to server!");

            String message;
            while ((message = in.readLine()) != null) {
                System.out.println(message);

                if (message.contains("Choice") ||
                        message.contains("Player Name") ||
                        message.contains("Set password") ||
                        message.contains("Enter password") ||
                        message.contains("Enter choice") ||
                        message.contains("YOUR MOVE")) {
                    System.out.print("-- ");
                    out.println(sc.nextLine());
                }
            }
            

        } catch (IOException e) {
            if (e.getMessage().contains("Connection refused")) {
                System.out.println("Cannot connect to server. Make sure the server is running!");
            } else {
                System.out.println("\nConnection lost. Game ended.");
            }
        }
    }
}
