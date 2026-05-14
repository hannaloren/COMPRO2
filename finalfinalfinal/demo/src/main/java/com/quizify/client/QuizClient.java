package com.quizify.client;

import com.quizify.model.Question;
import com.quizify.model.Receipt;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class QuizClient {

    public static void main(String[] args) {

        try (

                Socket socket = new Socket("localhost", 5000);

                ObjectOutputStream out = new ObjectOutputStream(
                        socket.getOutputStream());

                ObjectInputStream in = new ObjectInputStream(
                        socket.getInputStream());

                Scanner sc = new Scanner(System.in)

        ) {

            System.out.println();
            System.out.println("====================================");
            System.out.println("             QUIZIFY");
            System.out.println("====================================");

            System.out.print("Enter Name: ");
            out.writeObject(sc.nextLine());
            out.flush();

            System.out.print("Enter Code: ");
            out.writeObject(sc.nextLine());
            out.flush();

            String status = (String) in.readObject();

            if (status.equals("ACCESS_DENIED")) {

                System.out.println();
                System.out.println("ACCESS DENIED");
                return;
            }

            System.out.println();
            System.out.println();
            System.out.println("ACCESS GRANTED");
            System.out.println("Waiting for teacher...");

            // WAIT FOR SERVER START SIGNAL
            while (true) {

                Object obj = in.readObject();

                if (obj instanceof String msg) {

                    if (msg.equals("START_GAME")) {

                        System.out.println();
                        System.out.println("====================================");
                        System.out.println("         GAME STARTED!");
                        System.out.println("====================================");

                        break;
                    }
                }
            }

            // NOW RECEIVE TOTAL QUESTIONS
            int total = (Integer) in.readObject();

            for (int i = 0; i < total; i++) {

                Question q = (Question) in.readObject();

                int timer = (Integer) in.readObject();

                System.out.println();
                System.out.println("====================================");

                q.display();

                TimerThread tt = new TimerThread(timer);
                tt.start();

                System.out.println();
                System.out.print("Your Answer: ");

                AnswerThread at = new AnswerThread(sc);
                at.start();

                at.join(timer * 1000L);

                String answer;

                if (at.isAlive()) {

                    answer = "NO ANSWER";

                    System.out.println("\nTIME IS UP!");

                } else {

                    answer = at.answer;
                }

                // STOP TIMER
                tt.running = false;

                out.writeObject(answer);
                out.flush();
            }

            System.out.println();
            System.out.println(in.readObject());

            String finalMsg = (String) in.readObject();

            if (finalMsg.equals("RECEIPT_SAVED")) {

                System.out.println();
                System.out.println("====================================");
                System.out.println(" Receipt file generated successfully");
                System.out.println("====================================");
            }

        } catch (Exception e) {

            System.out.println(
                    "Disconnected from server.");
        }
    }
}