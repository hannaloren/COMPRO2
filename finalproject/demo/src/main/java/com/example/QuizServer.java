package com.example;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.lang.reflect.Type;

public class QuizServer {
    private static final int PORT = 12345;
    private static final int MAX_PLAYERS = 50;
    private static List<ClientHandler> clients = new CopyOnWriteArrayList<>();
    private static List<Question> questions = new ArrayList<>();
    private static volatile boolean gameStarted = false;

    public static void main(String[] args) {
        loadQuestions("questions.json");

        if (questions.isEmpty()) {
            System.out.println("No questions found. Please check your JSON path.");
            return;
        }

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {

            System.out.println("--- QUIZ IS ABOUT TO COMMENCE ---");
            System.out.println("Type 'START' and press Enter to begin the game.");

            // Thread to accept incoming connections
            Thread acceptThread = new Thread(() -> {
                while (!gameStarted && clients.size() < MAX_PLAYERS) {
                    try {
                        Socket socket = serverSocket.accept();
                        if (gameStarted) {
                            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                            out.println("GAME_ALREADY_STARTED");
                            socket.close();
                            continue;
                        }
                        ClientHandler client = new ClientHandler(socket);
                        clients.add(client);
                        client.start();
                        System.out.println("New player connected. Total: " + clients.size());

                    } catch (IOException e) {
                        if (!gameStarted)
                            System.err.println("Accept error: " + e.getMessage());
                    }

                }
                System.out.println(" " + generateLeaderboard());
            });
            acceptThread.start();

            // Admin Control Loop
            Scanner sc = new Scanner(System.in);
            while (true) {
                String cmd = sc.nextLine();
                if (cmd.equalsIgnoreCase("START")) {
                    if (clients.isEmpty()) {
                        System.out.println("Cannot start with 0 players!");
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

    private static void loadQuestions(String filename) {

        File file = new File("C:\\Users\\STUDENTS\\COMPRO2\\finalproject\\data\\questions.json");
        if (!file.exists()) {
            questions.add(new Question("What is 2+2?", "4"));
            questions.add(new Question("What is the capital of France?", "Paris"));
            return;
        }

        try (Reader reader = new FileReader(file)) {
            Type listType = new TypeToken<ArrayList<Question>>() {
            }.getType();
            questions = new Gson().fromJson(reader, listType);
            System.out.println("Loaded " + questions.size() + " questions.");
        } catch (IOException e) {
            System.err.println("Error reading JSON: " + e.getMessage());
        }
    }

    private static void runGame() {
        broadcast("\n----- WELCOME TO QUIZIFY! -----");

        for (int i = 0; i < questions.size(); i++) {
            Question q = questions.get(i);
            broadcast("\nQUESTION " + (i + 1) + ": " + q.getQuestion());
            broadcast("WAITING_FOR_ANSWER"); // Signal to clients to enable input

            // All clients answer in parallel
            clients.parallelStream().forEach(client -> {
                String response = client.receiveAnswer();
                if (response != null && response.equalsIgnoreCase(q.getAnswer())) {
                    client.sendMessage("CORRECT!");
                    client.addScore();
                } else {
                    client.sendMessage("WRONG! The answer was: " + q.getAnswer());
                }
            });
        }

        broadcast("       FINAL RESULTS " + generateLeaderboard());
        broadcast("GAME_OVER");
    }

    private static String generateLeaderboard() {
        List<ClientHandler> sortedClients = new ArrayList<>(clients);
        sortedClients.sort((c1, c2) -> Integer.compare(c2.getScore(), c1.getScore()));

        StringBuilder sb = new StringBuilder();
        sb.append("-------------------------------\n");
        for (ClientHandler c : sortedClients) {
            sb.append(String.format("%-15s | Score: %d\n", c.getPlayerName(), c.getScore()));
        }
        sb.append("-------------------------------");
        return sb.toString();
    }

    private static void broadcast(String msg) {
        for (ClientHandler client : clients) {
            client.sendMessage(msg);
        }
    }
}