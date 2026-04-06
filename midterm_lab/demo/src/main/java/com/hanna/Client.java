package com.hanna;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) {

        try (Socket socket = new Socket("localhost", 8000);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                Scanner sc = new Scanner(System.in)) {

            System.out.println("Connected to server!");

            System.out.print("Enter Username: ");
            out.println(sc.nextLine());

            System.out.print("Enter Password: ");
            out.println(sc.nextLine());

            // login success message
            System.out.println(in.readLine());

            // match with... message
            System.out.println(in.readLine());

            while (true) {

                String serverMessage = in.readLine();

                if (serverMessage == null || serverMessage.equals("Game Over")) {
                    System.out.println("Game Over!");
                    break;
                }

                System.out.println(serverMessage);
                String move = sc.nextLine();
                out.println(move);

                System.out.println(in.readLine()); // Winner line
                System.out.println(in.readLine()); // Score line                System.out.println(in.readLine()); // Dash line
            }

        } catch (Exception e) {
            System.out.println("Connection lost.");
        }
    }
}