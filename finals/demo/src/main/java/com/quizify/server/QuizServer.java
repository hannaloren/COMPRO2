package com.quizify.server;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import com.quizify.model.Identification;
import com.quizify.model.MultipleChoice;
import com.quizify.model.Question;
import com.quizify.model.TrueFalse;

public class QuizServer {

    public static List<Question> questions;

    public static List<String> students;

    public static volatile boolean quizStarted = false;

    public static CopyOnWriteArrayList<ClientHandler> clients = new CopyOnWriteArrayList<>();

    public static ConcurrentHashMap<String, ClientHandler> activeUsers = new ConcurrentHashMap<>();

    public static synchronized void checkIfAllFinished() {

        if (clients.isEmpty()) {
            return;
        }

        for (ClientHandler c : clients) {

            if (c.state != State.FINISHED) {
                return;
            }
        }

        System.out.println();
        System.out.println(
                "QUIZ FINISHED - ALL STUDENTS DONE");

        System.exit(0);
    }

    public static void addQuestionMenu(
            Scanner sc) {

        System.out.println();
        System.out.println("====================================");
        System.out.println("          ADD QUESTION");
        System.out.println("====================================");

        System.out.println("1. Multiple Choice");
        System.out.println("2. Identification");
        System.out.println("3. True/False");

        System.out.print("Choose Type: ");

        int choice = Integer.parseInt(sc.nextLine());

        if (choice == 1) {

            System.out.print("Question: ");
            String prompt = sc.nextLine();

            String[] options = new String[4];

            System.out.print("A: ");
            options[0] = sc.nextLine();

            System.out.print("B: ");
            options[1] = sc.nextLine();

            System.out.print("C: ");
            options[2] = sc.nextLine();

            System.out.print("D: ");
            options[3] = sc.nextLine();

            System.out.print(
                    "Correct Answer (A/B/C/D): ");

            String answer = sc.nextLine();

            questions.add(
                    new MultipleChoice(
                            prompt,
                            options,
                            answer));

            System.out.println(
                    "Question Added.");
        }

        else if (choice == 2) {

            System.out.print("Question: ");
            String prompt = sc.nextLine();

            System.out.print(
                    "Correct Answer: ");

            String answer = sc.nextLine();

            questions.add(
                    new Identification(
                            prompt,
                            answer));

            System.out.println(
                    "Question Added.");
        }

        else if (choice == 3) {

            System.out.print("Question: ");
            String prompt = sc.nextLine();

            System.out.print(
                    "Correct Answer (T/F): ");

            String answer = sc.nextLine();

            questions.add(
                    new TrueFalse(
                            prompt,
                            answer));

            System.out.println(
                    "Question Added.");
        }
    }

    public static void main(String[] args) {

        try {

            questions = QuestionLoader.load(
                    "C:\\Users\\Asus Vivobook\\COMPRO2\\finalproject\\demo\\src\\main\\java\\com\\quizify\\resources\\questions.json");

            students = StudentLoader.load(
                    "C:\\Users\\Asus Vivobook\\COMPRO2\\finalproject\\demo\\src\\main\\java\\com\\quizify\\resources\\students.json");

            ServerSocket server = new ServerSocket(5000);

            System.out.println(
                    "====================================");

            System.out.println(
                    "         QUIZIFY SERVER");

            System.out.println(
                    "====================================");

            System.out.println(
                    "Waiting for students...");

            System.out.println(
                    "====================================");

            new Thread(() -> {

                try {

                    while (true) {

                        Socket socket = server.accept();

                        ClientHandler handler = new ClientHandler(
                                socket);

                        new Thread(handler).start();
                    }

                } catch (Exception e) {

                    e.printStackTrace();
                }

            }).start();

            new Thread(() -> {

                try {

                    while (true) {

                        System.out.println();

                        System.out.println(
                                "=== QUIZ STATUS ===");

                        for (ClientHandler c : clients) {

                            System.out.println(
                                    c.username
                                            + " : "
                                            + c.state);
                        }
                        System.out.println("Type START to begin");
                        System.out.println("Type ADD to add question");
                        Thread.sleep(5000);
                    }

                } catch (Exception e) {

                    e.printStackTrace();
                }

            }).start();

            Scanner sc = new Scanner(System.in);

            while (true) {

                String cmd = sc.nextLine();

                if (cmd.equalsIgnoreCase(
                        "START")) {

                    quizStarted = true;

                    System.out.println();

                    System.out.println(
                            "QUIZ STARTED!");
                }

                else if (cmd.equalsIgnoreCase(
                        "ADD")) {

                    addQuestionMenu(sc);
                }
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}