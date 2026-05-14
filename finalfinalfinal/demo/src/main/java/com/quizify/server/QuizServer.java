package com.quizify.server;

import com.quizify.model.Question;
import com.quizify.model.Student;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.CopyOnWriteArrayList;

public class QuizServer {

    public static List<Question> questions;
    public static List<Student> students;

    public static volatile boolean gameStarted = false;

    public static CopyOnWriteArrayList<ClientHandler> clients = new CopyOnWriteArrayList<>();

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
        System.out.println("====================================");
        System.out.println(" QUIZ FINISHED - PLAYERS LEAVING");
        System.out.println("====================================");

        System.exit(0);
    }

    public static void main(String[] args) {

        try {

            questions = QuestionLoader.load(
                    "C:\\Users\\Asus Vivobook\\COMPRO2\\finalfinalfinal\\demo\\src\\main\\java\\com\\quizify\\resources\\questions.json");

            students = StudentLoader.load(
                    "C:\\Users\\Asus Vivobook\\COMPRO2\\finalfinalfinal\\demo\\src\\main\\java\\com\\quizify\\resources\\students.json");

            ServerSocket server = new ServerSocket(5000);

            System.out.println("====================================");
            System.out.println("         QUIZIFY SERVER");
            System.out.println("====================================");
            System.out.println("Waiting for students...");
            System.out.println("Type START to begin.");
            System.out.println("====================================");

            new Thread(() -> {

                try {

                    while (true) {

                        Socket socket = server.accept();

                        ClientHandler handler = new ClientHandler(socket);

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
                        System.out.println("========== STATUS ==========");

                        for (ClientHandler c : clients) {

                            System.out.println(
                                    c.username +
                                            " : " +
                                            c.state);
                        }

                        System.out.println("============================");

                        Thread.sleep(5000);

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }).start();

            Scanner sc = new Scanner(System.in);

            while (true) {

                String cmd = sc.nextLine();

                if (cmd.equalsIgnoreCase("START")) {

                    gameStarted = true;

                    System.out.println();
                    System.out.println("====================================");
                    System.out.println("         GAME STARTED");
                    System.out.println("====================================");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}