package com.quizify.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import com.quizify.model.Question;
import com.quizify.model.Receipt;

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

            System.out.print("Enter Name: ");
            out.writeObject(br.readLine());
            out.flush();

            System.out.print("Enter Code: ");
            out.writeObject(br.readLine());
            out.flush();

            String status = (String) in.readObject();
            if (status.equals("ACCESS_DENIED")) {

                System.out.println();
                System.out.println("ACCESS DENIED");
                System.out.println("Student not found.");

                socket.close();

                return;
            }

            if (status.equals("ACCOUNT_ALREADY_LOGGED_IN")) {
                System.out.println("Student is already logged in.");

                socket.close();

                return;
            }

            if (status.equals("ACCESS_GRANTED")) {

                System.out.println();
                System.out.println("ACCESS GRANTED");
                System.out.println(
                        "Waiting for teacher to start the quiz...");
            }
            System.out.println("ACCESS GRANTED");
            System.out.println(
                    "Waiting for teacher to start the quiz...");
            boolean waiting = true;

            while (waiting) {
                try {
                    Object obj = in.readObject();

                    if (obj instanceof String msg) {

                        if (msg.equals("START_QUIZ")) {
                            waiting = false;
                        }
                    }

                } catch (java.io.EOFException eof) {
                    System.out.println("Server closed connection unexpectedly.");
                    return;
                } catch (Exception e) {
                    System.out.println("Disconnected while waiting for quiz start.");
                    return;
                }
            }

            System.out.println();
            System.out.println("====================================");
            System.out.println("         QUIZ STARTED!");
            System.out.println("====================================");

            int total = (Integer) in.readObject();

            for (int i = 0; i < total; i++) {

                Question q = (Question) in.readObject();

                int timer = (Integer) in.readObject();

                System.out.println();
                System.out.println("====================================");

                q.display();

                System.out.println();

                String answer = "NO ANSWER";

                long startTime = System.currentTimeMillis();

                long lastDisplayedSecond = -1;

                while (true) {

                    long elapsed = (System.currentTimeMillis()
                            - startTime) / 1000;

                    long remaining = timer - elapsed;

                    if (remaining != lastDisplayedSecond
                            && remaining >= 0) {

                        System.out.print(
                                "\rTime Left: "
                                        + remaining
                                        + " seconds      ");

                        lastDisplayedSecond = remaining;
                    }

                    if (remaining <= 0) {

                        answer = "NO ANSWER";

                        break;
                    }

                    if (br.ready()) {

                        answer = br.readLine();

                        break;
                    }
                }

                System.out.println();

                if (answer.equals("NO ANSWER")) {

                    System.out.println(
                            "TIME IS UP!");
                }

                System.out.print("Your Answer: ");
                System.out.println(answer);

                out.writeObject(answer);
                out.flush();
            }

            String end = (String) in.readObject();

            System.out.println();
            System.out.println(end);

            Receipt receipt = (Receipt) in.readObject();

            String username = (String) in.readObject();

            int score = (Integer) in.readObject();

            receipt.print(username, score);

        } catch (Exception e) {

            System.out.println(
                    "\nDisconnected from server.");

            e.printStackTrace();
        }
    }
}