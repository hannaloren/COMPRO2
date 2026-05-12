package com.example;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class QuizClient {

    private static final String SERVER_IP = "localhost";

    private static final int PORT = 12345;

    private static volatile boolean answering = false;

    public static void main(String[] args) {

        try (

                Socket socket = new Socket(SERVER_IP, PORT);

                BufferedReader in = new BufferedReader(
                        new InputStreamReader(
                                socket.getInputStream()));

                PrintWriter out = new PrintWriter(
                        socket.getOutputStream(),
                        true);

                Scanner sc = new Scanner(System.in);

        ) {

            Thread inputThread = new Thread(() -> {

                while (true) {

                    try {

                        if (answering) {

                            System.out.print("\r");

                            String answer = sc.nextLine();

                            out.println(answer);

                            answering = false;
                        }

                        Thread.sleep(100);

                    } catch (Exception e) {

                        break;
                    }
                }
            });

            inputThread.start();

            while (true) {

                String msg = in.readLine();

                if (msg == null)
                    break;

                if (msg.equals("CLEAR")) {

                    for (int i = 0; i < 40; i++) {
                        System.out.println();
                    }
                }

                else if (msg.equals("ENTER_NAME")) {

                    System.out.print(
                            "Name: ");

                    out.println(sc.nextLine());
                }

                else if (msg.equals("ENTER_PASSWORD")) {

                    System.out.print(
                            "Password: ");

                    out.println(sc.nextLine());
                }

                else if (msg.equals("ENTER_CODE")) {

                    System.out.print(
                            "Student Code: ");

                    out.println(sc.nextLine());
                }

                else if (msg.equals("VERIFIED")) {

                    System.out.println(
                            "\nLOGIN SUCCESSFUL!");
                }

                else if (msg.equals("VERIFICATION_FAILED")) {

                    System.out.println(
                            "\nACCESS DENIED!");

                    break;
                }

                else if (msg.startsWith("TIMER:")) {

                    String time = msg.split(":")[1];

                    System.out.print(
                            "\rTime Remaining: "
                                    + String.format(
                                            "%-3s",
                                            time + "s"));
                }

                else if (msg.equals("ANSWER_NOW")) {

                    answering = true;

                    System.out.print(
                            "\n-> Your Answer: ");
                }

                else if (msg.equals("STOP_ANSWER")) {

                    answering = false;

                    System.out.println(
                            "\nTIME'S UP!");
                }

                else if (msg.equals("GAME_OVER")) {

                    System.out.println(
                            "\nQUIZ FINISHED!");

                    break;
                }

                else {

                    System.out.println(msg);
                }
            }

        } catch (IOException e) {

            System.out.println(
                    "Connection Error: "
                            + e.getMessage());
        }
    }
}