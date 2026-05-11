package com.example;

import com.google.gson.*;
import java.io.*;
import java.net.*;
import java.util.*;

public class QuizServer {

    static ArrayList<ClientHandler> clients = new ArrayList<>();
    static ArrayList<Question> questions = new ArrayList<>();

    static final int PORT = 8000;
    static final int QUESTION_TIME = 10;

    public static void main(String[] args) {

        loadQuestions();

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {

            System.out.println("---QUIZIFY SERVER STARTED---");
            System.out.println("Port: " + PORT);
            System.out.println("Waiting for students to connect...\n");

            // Accept students for 20 seconds
            long startTime = System.currentTimeMillis();

            while ((System.currentTimeMillis() - startTime) < 20000) {

                serverSocket.setSoTimeout(1000);

                try {
                    Socket socket = serverSocket.accept();

                    ClientHandler client = new ClientHandler(socket);
                    clients.add(client);
                    client.start();

                    broadcastPlayers();
                } catch (SocketTimeoutException e) {
                    // continue waiting
                }
            }

            System.out.println("\nQuiz starting now...\n");

            startQuiz();
            showLeaderboard();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ================= QUESTIONS =================

    public static void loadQuestions() {
        try {

            FileReader reader = new FileReader("C:\\Users\\STUDENTS\\COMPRO2\\finalproject\\data\\questions.json");

            Gson gson = new com.google.gson.Gson();

            Question[] loadedQuestions = gson.fromJson(reader, Question[].class);

            questions.addAll(Arrays.asList(loadedQuestions));

            System.out.println("Questions loaded successfully from JSON file.");

            reader.close();

        } catch (Exception e) {
            System.out.println("Error loading questions.");
            e.printStackTrace();
        }
    }

    public static void startQuiz() {

        int questionNumber = 1;

        for (Question q : questions) {
            System.out.println("Question " + questionNumber);
            System.out.println(q.getQuestion() + "\n");

            broadcast("QUESTION:" + q.getQuestion());
            broadcast("TIMER:" + QUESTION_TIME);

            // Countdown Thread
            Thread timerThread = new Thread(() -> {
                for (int i = QUESTION_TIME; i >= 1; i--) {
                    broadcast("COUNTDOWN:" + i);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });

            timerThread.start();
            ArrayList<Thread> answerThreads = new ArrayList<>();

            for (ClientHandler client : clients) {

                Thread t = new Thread(() -> {

                    String answer = client.receiveAnswer();

                    if (answer != null &&
                            answer.equalsIgnoreCase(q.getAnswer())) {

                        client.addScore();
                        client.sendMessage("RESULT: CORRECT");

                    } else {
                        client.sendMessage("RESULT: WRONG");
                    }
                });

                answerThreads.add(t);
                t.start();
            }

            // Wait all answers
            for (Thread t : answerThreads) {
                try {
                    t.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            try {
                timerThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            questionNumber++;
        }

        broadcast("QUIZ_OVER");
    }

    public static void showLeaderboard() {

        clients.sort((a, b) -> b.getScore() - a.getScore());

        System.out.println("---FINAL LEADERBOARD---");

        int rank = 1;

        for (ClientHandler client : clients) {

            String result = rank + ". " +
                    client.getPlayerName() +
                    " - " +
                    client.getScore() +
                    " pts";

            System.out.println(result);

            client.sendMessage("LEADERBOARD:" + result);
            rank++;
        }

        broadcast("END");
    }

    // ================= BROADCAST =================

    public static void broadcast(String msg) {
        for (ClientHandler client : clients) {
            client.sendMessage(msg);
        }
    }

    public static void broadcastPlayers() {

        String message = "PLAYERS:" + clients.size();

        for (ClientHandler client : clients) {
            client.sendMessage(message);
        }
    }
}
