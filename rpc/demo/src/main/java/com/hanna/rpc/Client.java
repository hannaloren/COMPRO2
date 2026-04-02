package com.hanna.rpc;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) {

        try (Socket socket = new Socket("192.168.1.87", 8000);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                Scanner sc = new Scanner(System.in)) {

            System.out.println("Connected to server!");

            // USERNAME
            System.out.print("Enter Username: ");
            String username = sc.nextLine();
            out.println(username);

            System.out.print("Enter Password: ");
            String password = sc.nextLine();
            out.println(password);

            System.out.println(in.readLine());

            boolean playing = true;

            while (playing) {
                String serverMessage = in.readLine();

                if (serverMessage.equals("Game Over")) {
                    System.out.println(serverMessage);
                    break;
                }

                System.out.println(serverMessage);

                String move = sc.nextLine();
                out.println(move);

                System.out.println(in.readLine());
                System.out.println(in.readLine());
            }

            // GAME OVER MESSAGE
            System.out.println(in.readLine());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
