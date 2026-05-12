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

    private static List<ClientHandler> clients = new CopyOnWriteArrayList<>();
    private static List<Question> questions = new ArrayList<>();
    private static List<Student> students = new ArrayList<>();

    private static Map<String, ClientHandler> activeClients = new HashMap<>();
    private static Map<String, Long> lastSeen = new HashMap<>();

    private static volatile boolean gameStarted = false;

    // 🔥 DASHBOARD STATE
    private static volatile String currentQuestion = "";
    private static volatile int questionIndex = 0;

    private static List<String> cheaters = new ArrayList<>();

    public static void main(String[] args) {

        loadStudents();
        loadQuestions();

        startTerminalDashboard();
        startInactivityChecker();

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {

            System.out.println("SERVER STARTED");

            Thread acceptThread = new Thread(() -> {
                while (!gameStarted) {
                    try {
                        Socket socket = serverSocket.accept();

                        ClientHandler client = new ClientHandler(socket);
                        clients.add(client);
                        client.start();

                        System.out.println("Client joined: " + clients.size());

                    } catch (Exception e) {
                        System.out.println("Accept error");
                    }
                }
            });

            acceptThread.start();

            Scanner sc = new Scanner(System.in);
            while (true) {
                if (sc.nextLine().equalsIgnoreCase("START")) {
                    gameStarted = true;
                    break;
                }
            }

            runGame();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ================= LOGIN =================

    public static synchronized boolean verifyStudent(String name, String pass, String code, ClientHandler handler) {

        String key = code.toLowerCase();

        if (activeClients.containsKey(key))
            return false;

        for (Student s : students) {
            if (s.getName().equalsIgnoreCase(name)
                    && s.getPassword().equals(pass)
                    && s.getCode().equalsIgnoreCase(code)) {

                activeClients.put(key, handler);
                lastSeen.put(key, System.currentTimeMillis());
                return true;
            }
        }

        return false;
    }

    public static void updateHeartbeat(String code) {
        lastSeen.put(code.toLowerCase(), System.currentTimeMillis());
    }

    public static synchronized void removeUser(String code) {
        activeClients.remove(code.toLowerCase());
        lastSeen.remove(code.toLowerCase());
    }

    public static void reportCheater(String name, String reason) {
        cheaters.add(name + " | " + reason);
    }

    // ================= GAME =================

    private static void runGame() {

        broadcast("=== QUIZ STARTED ===");

        for (Question q : questions) {

            questionIndex++;
            currentQuestion = q.getQuestion();

            for (ClientHandler c : clients) {
                c.resetAnswer();
                c.enableAnswering();
            }

            broadcast(q.getQuestion());
            broadcast("ANSWER_NOW");

            // 🔥 30 SECOND TIMER
            for (int t = 30; t >= 0; t--) {
                broadcast("TIMER:" + t);
                sleep(1000);
            }

            for (ClientHandler c : clients) {
                c.disableAnswering();
            }

            broadcast("STOP_ANSWER");

            // RESULTS
            System.out.println("\n===== QUESTION RESULT =====");
            System.out.println("Correct Answer: " + q.getAnswer());

            for (ClientHandler c : clients) {

                String ans = c.getLatestAnswer();

                System.out.println(c.getPlayerName()
                        + " | " + (ans == null ? "\nNO ANSWER" : ans));

                if (ans != null && ans.equalsIgnoreCase(q.getAnswer())) {
                    c.addScore();
                    c.sendMessage("\nCORRECT");
                } else {
                    c.sendMessage("\nWRONG");
                }
            }

            broadcastLeaderboard();

            sleep(2000);
        }

        broadcast("GAME_OVER");
    }

    private static void broadcastLeaderboard() {

        System.out.println("\nLEADERBOARD");

        clients.sort((a, b) -> b.getScore() - a.getScore());

        int rank = 1;

        for (ClientHandler c : clients) {
            System.out.println(rank + ". " + c.getPlayerName()
                    + " | " + c.getScore());
            rank++;
        }
    }

    private static void broadcast(String msg) {
        for (ClientHandler c : clients) {
            c.sendMessage(msg);
        }
    }

    private static void sleep(int ms) {
        try {
            Thread.sleep(ms);
        } catch (Exception ignored) {
        }
    }

    // ================= TERMINAL DASHBOARD =================

    private static void startTerminalDashboard() {

        Thread t = new Thread(() -> {

            while (true) {

                try {
                    Thread.sleep(2000);

                    System.out.print("\033[H\033[2J");
                    System.out.flush();

                    System.out.println("=========== QUIZ DASHBOARD ===========");

                    System.out.println("\nQUESTION:");
                    System.out.println(currentQuestion);
                    System.out.println("Progress: " + questionIndex + " / " + questions.size());

                    System.out.println("\nACTIVE USERS:");
                    for (ClientHandler c : clients) {
                        System.out.println("- " + c.getPlayerName()
                                + " | Score: " + c.getScore());
                    }

                    System.out.println("\nLEADERBOARD:");

                    clients.sort((a, b) -> b.getScore() - a.getScore());

                    int rank = 1;
                    for (ClientHandler c : clients) {
                        System.out.println(rank + ". " + c.getPlayerName()
                                + " | " + c.getScore());
                        rank++;
                    }

                    System.out.println("\nCHEATERS:");
                    if (cheaters.isEmpty()) {
                        System.out.println("- None");
                    } else {
                        for (String ch : cheaters) {
                            System.out.println("- " + ch);
                        }
                    }

                    System.out.println("\n=====================================");

                } catch (Exception e) {
                    break;
                }
            }
        });

        t.setDaemon(true);
        t.start();
    }

    // ================= INACTIVITY =================

    private static void startInactivityChecker() {

        Thread t = new Thread(() -> {

            while (true) {
                try {
                    Thread.sleep(3000);

                    long now = System.currentTimeMillis();

                    for (String code : new ArrayList<>(lastSeen.keySet())) {
                        if (now - lastSeen.get(code) > 60000) {
                            activeClients.remove(code);
                            lastSeen.remove(code);
                        }
                    }

                } catch (Exception e) {
                    break;
                }
            }
        });

        t.setDaemon(true);
        t.start();
    }

    // ================= LOAD DATA =================

    private static void loadStudents() {
        try (Reader r = new FileReader("data/students.json")) {
            Type t = new TypeToken<ArrayList<Student>>() {
            }.getType();
            students = new Gson().fromJson(r, t);
        } catch (Exception e) {
            System.out.println("Student load error");
        }
    }

    private static void loadQuestions() {
        try (Reader r = new FileReader("data/questions.json")) {
            Type t = new TypeToken<ArrayList<Question>>() {
            }.getType();
            questions = new Gson().fromJson(r, t);
        } catch (Exception e) {
            System.out.println("Question load error");
        }
    }
}