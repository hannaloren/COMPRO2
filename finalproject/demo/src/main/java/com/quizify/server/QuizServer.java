package com.quizify.server;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import com.quizify.model.Question;

public class QuizServer {

    public static List<Question> questions;
    public static List<String> students;

    public static volatile boolean quizStarted = false;

    public static CopyOnWriteArrayList<ClientHandler> clients = new CopyOnWriteArrayList<>();

    
    public static ConcurrentHashMap<String, ClientHandler> activeUsers = new ConcurrentHashMap<>();

    public static synchronized void checkIfAllFinished() {

        if (clients.isEmpty())
            return;

        for (ClientHandler c : clients) {
            if (c.state != State.FINISHED)
                return;
        }

        System.out.println("\nQUIZ FINISHED - ALL STUDENTS DONE");
        System.exit(0);
    }

    public static void main(String[] args) {

        try {

            questions = QuestionLoader.load(
                    "C:\\Users\\Asus Vivobook\\COMPRO2\\finalproject\\demo\\src\\main\\java\\com\\quizify\\resources\\questions.json");

            students = StudentLoader.load(
                    "C:\\Users\\Asus Vivobook\\COMPRO2\\finalproject\\demo\\src\\main\\java\\com\\quizify\\resources\\students.json");

            ServerSocket server = new ServerSocket(5000);
            System.out.println("===============================");
            System.out.println("QUIZIFY SERVER STARTED");
            System.out.println("Waiting for students...");
            System.out.println("===============================");

            // ACCEPT CLIENTS
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

            // STATUS THREAD
            new Thread(() -> {
                try {
                    while (true) {

                        System.out.println("\n=== QUIZ STATUS ===");

                        for (ClientHandler c : clients) {
                            System.out.println(
                                    (c.username == null ? "UNKNOWN" : c.username)
                                            + " : " + c.state);
                            System.out.println();
                        }
                        System.out.println("Type 'START' to begin the quiz.");
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
                    quizStarted = true;
                    System.out.println("\nQUIZ STARTED!");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}