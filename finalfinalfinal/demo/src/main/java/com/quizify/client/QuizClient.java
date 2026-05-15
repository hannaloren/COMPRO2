package com.quizify.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import com.quizify.model.Question;

public class QuizClient {

    public static void main(String[] args) {

        try (

                Socket socket = new Socket("localhost", 5000);

                ObjectOutputStream out = new ObjectOutputStream(
                        socket.getOutputStream());

                ObjectInputStream in = new ObjectInputStream(
                        socket.getInputStream());

                BufferedReader br = new BufferedReader(
                        new InputStreamReader(System.in))

        ) {

            System.out.println();
            System.out.println("====================================");
            System.out.println("             QUIZIFY");
            System.out.println("====================================");

            // AUTHENTICATION
            System.out.print("Enter Name: ");
            out.writeObject(br.readLine());
            out.flush();

            System.out.print("Enter Code: ");
            out.writeObject(br.readLine());
            out.flush();

            String status = (String) in.readObject();

            if (status.equals("ACCESS_DENIED")) {

                System.out.println("\nACCESS DENIED");
                return;
            }

            System.out.println("\nACCESS GRANTED");
            System.out.println("Waiting for teacher...");

            // WAIT FOR GAME START
            while (true) {

                String msg = (String) in.readObject();

                if (msg.equals("START_GAME")) {
                    break;
                }
            }

            System.out.println();
            System.out.println("         GAME STARTED!");
            System.out.println("====================================");

            int total = (Integer) in.readObject();

            // QUESTIONS LOOP
            for (int i = 0; i < total; i++) {

                Question q = (Question) in.readObject();

                int timer = (Integer) in.readObject();

                System.out.println();

                q.display();

                String answer = "NO ANSWER";

                // TIMER LOOP
                for (int t = timer; t >= 0; t--) {

                    System.out.print(
                            "\rTime Left: "
                                    + t
                                    + " seconds ");

                    long start = System.currentTimeMillis();

                    while (System.currentTimeMillis()
                            - start < 1000) {

                        // CHECK INPUT
                        if (br.ready()) {

                            answer = br.readLine();

                            t = -1;
                            break;
                        }
                    }
                }

                if (answer.equals("NO ANSWER")) {

                    System.out.println("\nTIME IS UP!");
                }

                out.writeObject(answer);
                out.flush();
            }

            String end = (String) in.readObject();

            System.out.println();
            System.out.println(end);

            String receiptMsg = (String) in.readObject();

            System.out.println(receiptMsg);

        } catch (Exception e) {

            System.out.println(
                    "\nDisconnected from server.");

            e.printStackTrace();
        }
    }
}