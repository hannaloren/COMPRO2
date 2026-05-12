package com.example;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class QuizClient {

    private static final String SERVER = "localhost";
    private static final int PORT = 12345;

    private static volatile boolean answering = false;

    public static void main(String[] args) {

        try (
                Socket socket = new Socket(SERVER, PORT);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                Scanner sc = new Scanner(System.in)) {

            Thread t = new Thread(() -> {
                while (true) {
                    try {
                        if (answering) {
                            out.println(sc.nextLine());
                            answering = false;
                        }
                        Thread.sleep(100);
                    } catch (Exception e) {
                        break;
                    }
                }
            });

            t.start();

            while (true) {

                String msg = in.readLine();
                if (msg == null)
                    break;

                if (msg.equals("ENTER_NAME")) {
                    System.out.print("Name: ");
                    out.println(sc.nextLine());
                }

                else if (msg.equals("ENTER_PASSWORD")) {
                    System.out.print("Password: ");
                    out.println(sc.nextLine());
                }

                else if (msg.equals("ENTER_CODE")) {
                    System.out.print("Code: ");
                    out.println(sc.nextLine());
                }

                else if (msg.startsWith("TIMER:")) {
                    System.out.print("\r " + msg + "   -> ");
                }

                else if (msg.equals("ANSWER_NOW")) {
                    answering = true;
                    System.out.print("\nAnswer: ");
                }

                else if (msg.equals("STOP_ANSWER")) {
                    answering = false;
                    System.out.println("\nTime up");
                }

                else if (msg.equals("GAME_OVER")) {
                    System.out.println("\nFinished");
                    break;
                }

                else {
                    System.out.println(msg);
                }
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}