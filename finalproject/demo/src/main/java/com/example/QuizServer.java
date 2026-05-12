package com.example;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class QuizServer {

    private static final int PORT = 12345;
    private static final int MAX_PLAYERS = 50;

    private static List<ClientHandler> clients = new CopyOnWriteArrayList<>();

    private static List<Question> questions = new ArrayList<>();

    private static List<Student> authorizedStudents = new ArrayList<>();

    private static volatile boolean gameStarted = false;

    public static void main(String[] args) {

        loadStudents();
        loadQuestions();

        if (questions.isEmpty()) {

            System.out.println("No questions found.");
            return;
        }

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {

            System.out.println(
                    "===== QUIZ SERVER STARTED =====");

            System.out.println("Waiting for players...");
            System.out.println("Type START to begin.");

            Thread acceptThread = new Thread(() -> {

                while (!gameStarted &&
                        clients.size() < MAX_PLAYERS) {

                    try {

                        Socket socket = serverSocket.accept();

                        if (gameStarted) {

                            PrintWriter out = new PrintWriter(
                                    socket.getOutputStream(),
                                    true);

                            out.println(
                                    "GAME_ALREADY_STARTED");

                            socket.close();

                            continue;
                        }

                        ClientHandler client = new ClientHandler(socket);

                        clients.add(client);

                        client.start();

                        System.out.println(
                                "Player connected. Total: "
                                        + clients.size());

                    } catch (IOException e) {

                        if (!gameStarted) {

                            System.out.println(
                                    "Accept Error: "
                                            + e.getMessage());
                        }
                    }
                }
            });

            acceptThread.start();

            Scanner sc = new Scanner(System.in);

            while (true) {

                String cmd = sc.nextLine();

                if (cmd.equalsIgnoreCase("START")) {

                    if (clients.isEmpty()) {

                        System.out.println(
                                "No players connected!");

                        continue;
                    }

                    gameStarted = true;

                    break;
                }
            }

            runGame();

        } catch (IOException e) {

            e.printStackTrace();
        }
    }

    private static void loadQuestions() {

        File file = new File(
                "data/questions.json");

        if (!file.exists()) {

            questions.add(
                    new Question(
                            "What is the capital of France?",
                            "Paris"));

            questions.add(
                    new Question(
                            "5 + 7 = ?",
                            "12"));

            return;
        }

        try (Reader reader = new FileReader(file)) {

            Type listType = new TypeToken<ArrayList<Question>>() {
            }.getType();

            questions = new Gson().fromJson(reader, listType);

            System.out.println(
                    "Loaded "
                            + questions.size()
                            + " questions.");

        } catch (IOException e) {

            System.out.println(
                    "Question Load Error: "
                            + e.getMessage());
        }
    }

    private static void loadStudents() {

        File file = new File(
                "data/students.json");

        if (!file.exists()) {

            System.out.println(
                    "students.json not found!");

            return;
        }

        try (Reader reader = new FileReader(file)) {

            Type listType = new TypeToken<ArrayList<Student>>() {
            }.getType();

            authorizedStudents = new Gson().fromJson(reader, listType);

            System.out.println(
                    "Loaded "
                            + authorizedStudents.size()
                            + " students.");

        } catch (IOException e) {

            System.out.println(
                    "Student Load Error: "
                            + e.getMessage());
        }
    }

    public static boolean verifyStudent(
            String name,
            String password,
            String code) {

        for (Student s : authorizedStudents) {

            boolean matchName = s.getName()
                    .equalsIgnoreCase(name.trim());

            boolean matchPassword = s.getPassword()
                    .equals(password.trim());

            boolean matchCode = s.getCode()
                    .equalsIgnoreCase(code.trim());

            if (matchName &&
                    matchPassword &&
                    matchCode) {

                return true;
            }
        }

        return false;
    }

    private static void runGame() {

        broadcast("\n=================================");
        broadcast("        WELCOME TO QUIZIFY");
        broadcast("\n=================================");

        for (int i = 0; i < questions.size(); i++) {

            Question q = questions.get(i);

            for (ClientHandler c : clients) {
                c.resetAnswer();
            }

            broadcast("\n=================================");
            broadcast("");
            broadcast("         QUESTION " + (i + 1));
            broadcast("\n=================================");

            broadcast("");
            broadcast(q.getQuestion());
            broadcast("");

            broadcast("ANSWER_NOW");

            for (int time = 10; time >= 0; time--) {

                broadcast("TIMER:" + time);

                try {

                    Thread.sleep(1000);

                } catch (InterruptedException e) {

                    e.printStackTrace();
                }
            }

            broadcast("STOP_ANSWER");

            for (ClientHandler client : clients) {

                String response = client.getLatestAnswer();

                if (response == null) {

                    client.sendMessage(
                            "\nNO ANSWER!");

                } else if (response.equalsIgnoreCase(
                        q.getAnswer())) {

                    client.sendMessage(
                            "\nCORRECT!");

                    client.addScore();

                } else {

                    client.sendMessage(
                            "\nWRONG!");
                }
            }

            broadcast(generateLeaderboard());

            try {

                Thread.sleep(3000);

            } catch (InterruptedException e) {

                e.printStackTrace();
            }
        }

        broadcast("\n=================================");
        broadcast("           GAME OVER");
        broadcast("=================================");

        broadcast(generateLeaderboard());

        broadcast("GAME_OVER");
    }

    private static String generateLeaderboard() {

        List<ClientHandler> sorted = new ArrayList<>(clients);

        sorted.sort((a, b) -> Integer.compare(
                b.getScore(),
                a.getScore()));

        StringBuilder sb = new StringBuilder();

        sb.append("\n===== FINAL RESULTS =====\n");

        for (ClientHandler c : sorted) {

            sb.append(c.getPlayerName())
                    .append(" | Score: ")
                    .append(c.getScore())
                    .append("\n");
        }

        return sb.toString();
    }

    private static void broadcast(String msg) {

        for (ClientHandler client : clients) {
            client.sendMessage(msg);
        }
    }
}